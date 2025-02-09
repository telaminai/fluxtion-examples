/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.runtime.agentrunner;

import com.fluxtion.agrona.concurrent.Agent;
import com.fluxtion.dataflow.runtime.input.NamedFeed;

public interface EventFeedAgent extends NamedFeed, Agent {

}