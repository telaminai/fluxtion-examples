/*
 * SPDX-File Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com>
 * SPDX-License-Identifier: GPL-3.0-only
 */

package com.fluxtion.dataflow.examples.frontpage.multijoin.userfunc;

public record SupportContactEvent(String name, LocationCode locationCode, String contactDetails) {
}
