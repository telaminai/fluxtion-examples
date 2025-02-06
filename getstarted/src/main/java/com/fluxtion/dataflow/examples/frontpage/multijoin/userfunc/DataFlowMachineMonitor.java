/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.examples.frontpage.multijoin.userfunc;

/**
 * Monitors each machine for an average or current temperature breach in a sliding window of 4 seconds with a bucket size of 1 second
 * readings are produced randomly every 10 millis the aggregation handles all combining values within a window and dropping
 * values that have expired.<br>
 * <br>
 * Alarm status is published on any change to the alarm state, i.e. new alarms or cleared old alarms<br>
 * <br>
 * Each machine can have its own temperature alarm profile updated by event MachineProfile<br>
 * <br>
 * Notifies a support contact in the correct location where the breach has occurred. The contact lookup is built up
 * through events:
 * <ul>
 *     <li>MachineLocation = Machine id -> location</li>
 *     <li>SupportContact = location -> contact details</li>
 * </ul>
 * <br>
 * <br>
 * <p>
 * A sink is available for the host application to consume the alarm output, in this case a pretty print consumer<br>
 * <br>
 * <p>
 * Running the app should produce an output similar to below:
 *
 * <pre>
 *  Application started - wait four seconds for first machine readings
 *
 *  ALARM UPDATE 14:31:30.785
 *  New alarms: ['server_GOOG@USA_EAST_1',  temp:'49.16', avgTemp:'52.05' SupportContact[name=Jean, locationCode=USA_EAST_1, contactDetails=jean@fluxtion.com], 'server_TKM@USA_EAST_2',  temp:'86.47', avgTemp:'52.37' SupportContact[name=Tandy, locationCode=USA_EAST_2, contactDetails=tandy@fluxtion.com], 'server_AMZN@USA_EAST_1',  temp:'71.48', avgTemp:'54.25' SupportContact[name=Jean, locationCode=USA_EAST_1, contactDetails=jean@fluxtion.com], 'server_MSFT@USA_EAST_2',  temp:'31.70', avgTemp:'52.53' SupportContact[name=Tandy, locationCode=USA_EAST_2, contactDetails=tandy@fluxtion.com]]
 *  Alarms to clear[]
 *  Current alarms[server_GOOG, server_TKM, server_AMZN, server_MSFT]
 *  ------------------------------------
 *
 *  ALARM UPDATE 14:31:32.778
 *  New alarms: []
 *  Alarms to clear[server_TKM]
 *  Current alarms[server_GOOG, server_AMZN, server_MSFT]
 *  ------------------------------------
 *
 *  ALARM UPDATE 14:31:33.768
 *  New alarms: ['server_TKM@USA_EAST_2',  temp:'98.33', avgTemp:'49.95' SupportContact[name=Tandy, locationCode=USA_EAST_2, contactDetails=tandy@fluxtion.com]]
 *  Alarms to clear[]
 *  Current alarms[server_GOOG, server_TKM, server_AMZN, server_MSFT]
 *  ------------------------------------
 *
 *  ALARM UPDATE 14:31:37.777
 *  New alarms: []
 *  Alarms to clear[server_AMZN]
 *  Current alarms[server_GOOG, server_TKM, server_MSFT]
 *  ------------------------------------
 * </pre>
 */
public class DataFlowMachineMonitor {


}
