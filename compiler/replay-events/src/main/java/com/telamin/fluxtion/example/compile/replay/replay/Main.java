/*
 * Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com> - All Rights Reserved
 * This source code is protected under international copyright law.  All rights
 * reserved and protected by the copyright holders.
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holders.  If you encounter this file and do not have
 * permission, please contact the copyright holders and delete this file.
 */

package com.telamin.fluxtion.example.compile.replay.replay;

import com.telamin.fluxtion.builder.compile.generation.SourceGenerator;

import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<SourceGenerator> loader = ServiceLoader.load(SourceGenerator.class);

        loader.forEach(System.out::println);
    }
}
