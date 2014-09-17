/**
 * Copyright 2012-2014 Cask, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.tigon.sql.io;

import co.cask.tigon.internal.io.DatumReader;
import co.cask.tigon.internal.io.ReflectionDatumReader;
import co.cask.tigon.internal.io.ReflectionSchemaGenerator;
import co.cask.tigon.internal.io.Schema;
import co.cask.tigon.internal.io.UnsupportedTypeException;
import co.cask.tigon.io.Decoder;
import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * POJOCreator
 * This class converts incoming byte records into POJOs of the output class type
 */
public class POJOCreator {
  private static final Logger LOG = LoggerFactory.getLogger(POJOCreator.class);
  private final Schema schema;
  private final DatumReader outputGenerator;
  private final Class<?> outputClass;

  /**
   * Constructor for the POJOCreator
   * @param outputClass Class type to be generated by this {@link co.cask.tigon.sql.io.POJOCreator} object
   * @param schema {@link co.cask.tigon.sql.flowlet.StreamSchema} of the incoming data record
   * @throws UnsupportedTypeException if the {@link co.cask.tigon.internal.io.ReflectionDatumReader} cannot
   * instantiate an object of type outputClass
   */
  public POJOCreator(Class<?> outputClass, Schema schema) throws UnsupportedTypeException {
    this.schema = schema;
    this.outputClass = outputClass;
    this.outputGenerator = new ReflectionDatumReader(new ReflectionSchemaGenerator().generate(outputClass, false),
                                                     TypeToken.of(outputClass));
  }

  /**
   * This function is called for each incoming byte record.
   *
   * @param decoder The decoder that encapsulates the byte[] data record
   * @return Map of method and the input parameter objects
   * @throws java.io.IOException if the {@link co.cask.tigon.internal.io.ReflectionDatumReader} cannot decode incoming
   * data record
   */
  public Object decode(Decoder decoder) throws IOException {
    try {
      return outputGenerator.read(decoder, schema);
    } catch (IOException e) {
      LOG.error("Cannot instantiate object of type {}", outputClass.getName(), e);
      throw e;
    }
  }
}
