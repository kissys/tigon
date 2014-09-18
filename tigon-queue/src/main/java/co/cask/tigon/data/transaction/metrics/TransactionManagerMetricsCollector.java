/*
 * Copyright 2014 Cask Data, Inc.
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

package co.cask.tigon.data.transaction.metrics;

import com.continuuity.tephra.metrics.TxMetricsCollector;
import co.cask.tigon.metrics.MetricsCollectionService;
import co.cask.tigon.metrics.MetricsCollector;
import co.cask.tigon.metrics.MetricsScope;
import com.google.inject.Inject;

/**
 * Implementation for TxMetricsCollector that delegate the the underlying {@link MetricsCollector}.
 */
public class TransactionManagerMetricsCollector extends TxMetricsCollector {
  private final MetricsCollector metricsCollector;

  @Inject
  public TransactionManagerMetricsCollector(MetricsCollectionService metricsCollectionService) {
    this.metricsCollector = metricsCollectionService.getCollector(MetricsScope.SYSTEM, "transactions", "0");
  }

  @Override
  public void gauge(String metricName, int value, String...tags) {
    metricsCollector.gauge(metricName, value, tags);
  }

}