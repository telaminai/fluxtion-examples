/*
 * Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com> - All Rights Reserved
 * This source code is protected under international copyright law.  All rights
 * reserved and protected by the copyright holders.
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holders.  If you encounter this file and do not have
 * permission, please contact the copyright holders and delete this file.
 */
package com.telamin.fluxtion.example.compile.replay.replay.generated;

import com.telamin.fluxtion.builder.replay.YamlReplayRecordWriter;
import com.telamin.fluxtion.example.compile.replay.replay.BookPnl;
import com.telamin.fluxtion.example.compile.replay.replay.GlobalPnl;
import com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate;
import com.telamin.fluxtion.runtime.CloneableDataFlow;
import com.telamin.fluxtion.runtime.DataFlow;
import com.telamin.fluxtion.runtime.annotations.ExportService;
import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.audit.Auditor;
import com.telamin.fluxtion.runtime.audit.EventLogManager;
import com.telamin.fluxtion.runtime.audit.NodeNameAuditor;
import com.telamin.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.telamin.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.telamin.fluxtion.runtime.callback.InternalEventProcessor;
import com.telamin.fluxtion.runtime.context.DataFlowContext;
import com.telamin.fluxtion.runtime.event.Event;
import com.telamin.fluxtion.runtime.input.EventFeed;
import com.telamin.fluxtion.runtime.input.SubscriptionManager;
import com.telamin.fluxtion.runtime.input.SubscriptionManagerNode;
import com.telamin.fluxtion.runtime.lifecycle.BatchHandler;
import com.telamin.fluxtion.runtime.lifecycle.Lifecycle;
import com.telamin.fluxtion.runtime.node.ForkedTriggerTask;
import com.telamin.fluxtion.runtime.node.MutableDataFlowContext;
import com.telamin.fluxtion.runtime.service.ServiceListener;
import com.telamin.fluxtion.runtime.service.ServiceRegistryNode;
import com.telamin.fluxtion.runtime.time.Clock;
import com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : Not available
 * eventProcessorGenerator version : ${generator_version_information}
 * api version                     : ${api_version_information}
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.telamin.fluxtion.builder.generation.model.ExportFunctionMarker
 *   <li>com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate
 *   <li>com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GlobalPnlProcessor
        implements CloneableDataFlow<GlobalPnlProcessor>,
        /*--- @ExportService start ---*/
        @ExportService ServiceListener,
        /*--- @ExportService end ---*/
        DataFlow,
        InternalEventProcessor,
        BatchHandler {

    //Node declarations
    private final transient BookPnl bookPnl_book1 =
            new com.telamin.fluxtion.example.compile.replay.replay.BookPnl("book1");;
    private final transient BookPnl bookPnl_bookAAA =
            new com.telamin.fluxtion.example.compile.replay.replay.BookPnl("bookAAA");;
    private final transient BookPnl bookPnl_book_XYZ =
            new com.telamin.fluxtion.example.compile.replay.replay.BookPnl("book_XYZ");;
    private final transient CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
    public final transient Clock clock = new Clock();
    private final transient GlobalPnl globalPnl =
            new com.telamin.fluxtion.example.compile.replay.replay.GlobalPnl(
                    new ArrayList<>(Arrays.asList(bookPnl_book1, bookPnl_bookAAA, bookPnl_book_XYZ)));;
    public final transient NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
    private final transient SubscriptionManagerNode subscriptionManager =
            new SubscriptionManagerNode();
    private final transient MutableDataFlowContext context =
            new com.telamin.fluxtion.runtime.node.MutableDataFlowContext(
                    nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);;
    public final transient ServiceRegistryNode serviceRegistry = new ServiceRegistryNode();
    public final transient YamlReplayRecordWriter yamlReplayRecordWriter =
            new com.telamin.fluxtion.builder.replay.YamlReplayRecordWriter(clock);;
    private final transient ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
    //Dirty flags
    private boolean initCalled = false;
    private boolean processing = false;
    private boolean buffering = false;
    private final transient IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
            new IdentityHashMap<>(4);
    private final transient IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
            new IdentityHashMap<>(4);

    private boolean isDirty_bookPnl_book1 = false;
    private boolean isDirty_bookPnl_bookAAA = false;
    private boolean isDirty_bookPnl_book_XYZ = false;
    private boolean isDirty_clock = false;

    //Forked declarations

    //Filter constants

    //unknown event handler
    private Consumer unKnownEventHandler = (e) -> {};

    public GlobalPnlProcessor(Map<Object, Object> contextMap) {
        if (context != null) {
            context.replaceMappings(contextMap);
        }
        yamlReplayRecordWriter.setClassBlackList(new HashSet<>(Arrays.asList()));
        yamlReplayRecordWriter.setClassWhiteList(
                new HashSet<>(
                        Arrays.asList(com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate.class)));
        globalPnl.clock = clock;
        context.setClock(clock);
        serviceRegistry.setDataFlowContext(context);
        //node auditors
        initialiseAuditor(clock);
        initialiseAuditor(yamlReplayRecordWriter);
        initialiseAuditor(nodeNameLookup);
        initialiseAuditor(serviceRegistry);
        if (subscriptionManager != null) {
            subscriptionManager.setSubscribingEventProcessor(this);
        }
        if (context != null) {
            context.setEventProcessorCallback(this);
        }
    }

    public GlobalPnlProcessor() {
        this(null);
    }

    @Override
    public void init() {
        initCalled = true;
        auditEvent(Lifecycle.LifecycleEvent.Init);
        //initialise dirty lookup map
        isDirty("test");
        clock.init();
        afterEvent();
    }

    @Override
    public void start() {
        if (!initCalled) {
            throw new RuntimeException("init() must be called before start()");
        }
        processing = true;
        auditEvent(Lifecycle.LifecycleEvent.Start);
        globalPnl.start();
        afterEvent();
        callbackDispatcher.dispatchQueuedCallbacks();
        processing = false;
    }

    @Override
    public void startComplete() {
        if (!initCalled) {
            throw new RuntimeException("init() must be called before startComplete()");
        }
        processing = true;
        auditEvent(Lifecycle.LifecycleEvent.StartComplete);

        afterEvent();
        callbackDispatcher.dispatchQueuedCallbacks();
        processing = false;
    }

    @Override
    public void stop() {
        if (!initCalled) {
            throw new RuntimeException("init() must be called before stop()");
        }
        processing = true;
        auditEvent(Lifecycle.LifecycleEvent.Stop);

        afterEvent();
        callbackDispatcher.dispatchQueuedCallbacks();
        processing = false;
    }

    @Override
    public void tearDown() {
        initCalled = false;
        auditEvent(Lifecycle.LifecycleEvent.TearDown);
        serviceRegistry.tearDown();
        nodeNameLookup.tearDown();
        yamlReplayRecordWriter.tearDown();
        clock.tearDown();
        subscriptionManager.tearDown();
        afterEvent();
    }

    @Override
    public void setContextParameterMap(Map<Object, Object> newContextMapping) {
        context.replaceMappings(newContextMapping);
    }

    @Override
    public void addContextParameter(Object key, Object value) {
        context.addMapping(key, value);
    }

    //EVENT DISPATCH - START
    @Override
    @OnEventHandler(failBuildIfMissingBooleanReturn = false)
    public void onEvent(Object event) {
        if (buffering) {
            triggerCalculation();
        }
        if (processing) {
            callbackDispatcher.queueReentrantEvent(event);
        } else {
            processing = true;
            onEventInternal(event);
            callbackDispatcher.dispatchQueuedCallbacks();
            processing = false;
        }
    }

    @Override
    public void onEventInternal(Object event) {
        if (event instanceof com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate) {
            PnlUpdate typedEvent = (PnlUpdate) event;
            handleEvent(typedEvent);
        } else if (event
                instanceof com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
            ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
            handleEvent(typedEvent);
        } else {
            unKnownEventHandler(event);
        }
    }

    public void handleEvent(PnlUpdate typedEvent) {
        auditEvent(typedEvent);
        switch (typedEvent.filterString()) {
            //Event Class:[com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate] filterString:[book1]
            case ("book1"):
                handle_PnlUpdate_book1(typedEvent);
                afterEvent();
                return;
            //Event Class:[com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate] filterString:[bookAAA]
            case ("bookAAA"):
                handle_PnlUpdate_bookAAA(typedEvent);
                afterEvent();
                return;
            //Event Class:[com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate] filterString:[book_XYZ]
            case ("book_XYZ"):
                handle_PnlUpdate_book_XYZ(typedEvent);
                afterEvent();
                return;
        }
        afterEvent();
    }

    public void handleEvent(ClockStrategyEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_clock = true;
        clock.setClockStrategy(typedEvent);
        afterEvent();
    }
    //EVENT DISPATCH - END

    //FILTERED DISPATCH - START
    private void handle_PnlUpdate_book1(PnlUpdate typedEvent) {
        isDirty_bookPnl_book1 = bookPnl_book1.pnlUpdate(typedEvent);
        if (guardCheck_globalPnl()) {
            globalPnl.calculate();
        }
    }

    private void handle_PnlUpdate_bookAAA(PnlUpdate typedEvent) {
        isDirty_bookPnl_bookAAA = bookPnl_bookAAA.pnlUpdate(typedEvent);
        if (guardCheck_globalPnl()) {
            globalPnl.calculate();
        }
    }

    private void handle_PnlUpdate_book_XYZ(PnlUpdate typedEvent) {
        isDirty_bookPnl_book_XYZ = bookPnl_book_XYZ.pnlUpdate(typedEvent);
        if (guardCheck_globalPnl()) {
            globalPnl.calculate();
        }
    }
    //FILTERED DISPATCH - END

    //EXPORTED SERVICE FUNCTIONS - START
    @Override
    public void deRegisterService(com.telamin.fluxtion.runtime.service.Service<?> arg0) {
        beforeServiceCall(
                "public void com.telamin.fluxtion.runtime.service.ServiceRegistryNode.deRegisterService(com.telamin.fluxtion.runtime.service.Service<?>)");
        ExportFunctionAuditEvent typedEvent = functionAudit;
        serviceRegistry.deRegisterService(arg0);
        afterServiceCall();
    }

    @Override
    public void registerService(com.telamin.fluxtion.runtime.service.Service<?> arg0) {
        beforeServiceCall(
                "public void com.telamin.fluxtion.runtime.service.ServiceRegistryNode.registerService(com.telamin.fluxtion.runtime.service.Service<?>)");
        ExportFunctionAuditEvent typedEvent = functionAudit;
        serviceRegistry.registerService(arg0);
        afterServiceCall();
    }
    //EXPORTED SERVICE FUNCTIONS - END

    //EVENT BUFFERING - START
    public void bufferEvent(Object event) {
        buffering = true;
        if (event instanceof com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate) {
            PnlUpdate typedEvent = (PnlUpdate) event;
            auditEvent(typedEvent);
            switch (typedEvent.filterString()) {
                //Event Class:[com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate] filterString:[book1]
                case ("book1"):
                    handle_PnlUpdate_book1_bufferDispatch(typedEvent);
                    afterEvent();
                    return;
                //Event Class:[com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate] filterString:[bookAAA]
                case ("bookAAA"):
                    handle_PnlUpdate_bookAAA_bufferDispatch(typedEvent);
                    afterEvent();
                    return;
                //Event Class:[com.telamin.fluxtion.example.compile.replay.replay.PnlUpdate] filterString:[book_XYZ]
                case ("book_XYZ"):
                    handle_PnlUpdate_book_XYZ_bufferDispatch(typedEvent);
                    afterEvent();
                    return;
            }
        } else if (event
                instanceof com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
            ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
            auditEvent(typedEvent);
            isDirty_clock = true;
            clock.setClockStrategy(typedEvent);
        }
    }

    private void handle_PnlUpdate_book1_bufferDispatch(PnlUpdate typedEvent) {
        isDirty_bookPnl_book1 = bookPnl_book1.pnlUpdate(typedEvent);
    }

    private void handle_PnlUpdate_bookAAA_bufferDispatch(PnlUpdate typedEvent) {
        isDirty_bookPnl_bookAAA = bookPnl_bookAAA.pnlUpdate(typedEvent);
    }

    private void handle_PnlUpdate_book_XYZ_bufferDispatch(PnlUpdate typedEvent) {
        isDirty_bookPnl_book_XYZ = bookPnl_book_XYZ.pnlUpdate(typedEvent);
    }

    public void triggerCalculation() {
        buffering = false;
        String typedEvent = "No event information - buffered dispatch";
        if (guardCheck_globalPnl()) {
            globalPnl.calculate();
        }
        afterEvent();
    }
    //EVENT BUFFERING - END

    private void auditEvent(Object typedEvent) {
        clock.eventReceived(typedEvent);
        yamlReplayRecordWriter.eventReceived(typedEvent);
        nodeNameLookup.eventReceived(typedEvent);
        serviceRegistry.eventReceived(typedEvent);
    }

    private void auditEvent(Event typedEvent) {
        clock.eventReceived(typedEvent);
        yamlReplayRecordWriter.eventReceived(typedEvent);
        nodeNameLookup.eventReceived(typedEvent);
        serviceRegistry.eventReceived(typedEvent);
    }

    private void initialiseAuditor(Auditor auditor) {
        auditor.init();
        auditor.nodeRegistered(bookPnl_book1, "bookPnl_book1");
        auditor.nodeRegistered(bookPnl_bookAAA, "bookPnl_bookAAA");
        auditor.nodeRegistered(bookPnl_book_XYZ, "bookPnl_book_XYZ");
        auditor.nodeRegistered(globalPnl, "globalPnl");
        auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
        auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
        auditor.nodeRegistered(context, "context");
    }

    private void beforeServiceCall(String functionDescription) {
        functionAudit.setFunctionDescription(functionDescription);
        auditEvent(functionAudit);
        if (buffering) {
            triggerCalculation();
        }
        processing = true;
    }

    private void afterServiceCall() {
        afterEvent();
        callbackDispatcher.dispatchQueuedCallbacks();
        processing = false;
    }

    private void afterEvent() {

        clock.processingComplete();
        yamlReplayRecordWriter.processingComplete();
        nodeNameLookup.processingComplete();
        serviceRegistry.processingComplete();
        isDirty_bookPnl_book1 = false;
        isDirty_bookPnl_bookAAA = false;
        isDirty_bookPnl_book_XYZ = false;
        isDirty_clock = false;
    }

    @Override
    public void batchPause() {
        auditEvent(Lifecycle.LifecycleEvent.BatchPause);
        processing = true;

        afterEvent();
        callbackDispatcher.dispatchQueuedCallbacks();
        processing = false;
    }

    @Override
    public void batchEnd() {
        auditEvent(Lifecycle.LifecycleEvent.BatchEnd);
        processing = true;

        afterEvent();
        callbackDispatcher.dispatchQueuedCallbacks();
        processing = false;
    }

    @Override
    public boolean isDirty(Object node) {
        return dirtySupplier(node).getAsBoolean();
    }

    @Override
    public BooleanSupplier dirtySupplier(Object node) {
        if (dirtyFlagSupplierMap.isEmpty()) {
            dirtyFlagSupplierMap.put(bookPnl_book1, () -> isDirty_bookPnl_book1);
            dirtyFlagSupplierMap.put(bookPnl_bookAAA, () -> isDirty_bookPnl_bookAAA);
            dirtyFlagSupplierMap.put(bookPnl_book_XYZ, () -> isDirty_bookPnl_book_XYZ);
            dirtyFlagSupplierMap.put(clock, () -> isDirty_clock);
        }
        return dirtyFlagSupplierMap.getOrDefault(node, DataFlow.ALWAYS_FALSE);
    }

    @Override
    public void setDirty(Object node, boolean dirtyFlag) {
        if (dirtyFlagUpdateMap.isEmpty()) {
            dirtyFlagUpdateMap.put(bookPnl_book1, (b) -> isDirty_bookPnl_book1 = b);
            dirtyFlagUpdateMap.put(bookPnl_bookAAA, (b) -> isDirty_bookPnl_bookAAA = b);
            dirtyFlagUpdateMap.put(bookPnl_book_XYZ, (b) -> isDirty_bookPnl_book_XYZ = b);
            dirtyFlagUpdateMap.put(clock, (b) -> isDirty_clock = b);
        }
        dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
    }

    private boolean guardCheck_yamlReplayRecordWriter() {
        return isDirty_clock;
    }

    private boolean guardCheck_globalPnl() {
        return isDirty_bookPnl_book1
                | isDirty_bookPnl_bookAAA
                | isDirty_bookPnl_book_XYZ
                | isDirty_clock;
    }

    private boolean guardCheck_context() {
        return isDirty_clock;
    }

    @Override
    public <T> T getNodeById(String id) throws NoSuchFieldException {
        return nodeNameLookup.getInstanceById(id);
    }

    @Override
    public <A extends Auditor> A getAuditorById(String id)
            throws NoSuchFieldException, IllegalAccessException {
        return (A) this.getClass().getField(id).get(this);
    }

    @Override
    public void addEventFeed(EventFeed eventProcessorFeed) {
        subscriptionManager.addEventProcessorFeed(eventProcessorFeed);
    }

    @Override
    public void removeEventFeed(EventFeed eventProcessorFeed) {
        subscriptionManager.removeEventProcessorFeed(eventProcessorFeed);
    }

    @Override
    public GlobalPnlProcessor newInstance() {
        return new GlobalPnlProcessor();
    }

    @Override
    public GlobalPnlProcessor newInstance(Map<Object, Object> contextMap) {
        return new GlobalPnlProcessor();
    }

    @Override
    public String getLastAuditLogRecord() {
        try {
            EventLogManager eventLogManager =
                    (EventLogManager) this.getClass().getField(EventLogManager.NODE_NAME).get(this);
            return eventLogManager.lastRecordAsString();
        } catch (Throwable e) {
            return "";
        }
    }

    public void unKnownEventHandler(Object object) {
        unKnownEventHandler.accept(object);
    }

    @Override
    public <T> void setUnKnownEventHandler(Consumer<T> consumer) {
        unKnownEventHandler = consumer;
    }

    @Override
    public SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }
}
