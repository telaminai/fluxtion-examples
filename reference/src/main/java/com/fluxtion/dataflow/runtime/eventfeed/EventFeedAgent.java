/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.eventfeed;

import com.fluxtion.agrona.concurrent.Agent;
import com.fluxtion.dataflow.runtime.annotations.feature.Experimental;
import com.fluxtion.dataflow.runtime.input.NamedFeed;

@Experimental
public interface EventFeedAgent extends NamedFeed, Agent {

}