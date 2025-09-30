/*
 * Copyright: © 2025.  Gregory Higgins <greg.higgins@v12technology.com> - All Rights Reserved
 * This source code is protected under international copyright law.  All rights
 * reserved and protected by the copyright holders.
 * This file is confidential and only available to authorized individuals with the
 * permission of the copyright holders.  If you encounter this file and do not have
 * permission, please contact the copyright holders and delete this file.
 */
package com.telamin.fluxtion.example.compile.aot.generated;

import com.telamin.fluxtion.example.compile.aot.node.LevelsCalculator;
import com.telamin.fluxtion.example.compile.aot.node.MidCalculator;
import com.telamin.fluxtion.example.compile.aot.node.PriceLadderPublisher;
import com.telamin.fluxtion.example.compile.aot.node.SkewCalculator;
import com.telamin.fluxtion.example.compile.aot.pricer.PriceCalculator;
import com.telamin.fluxtion.example.compile.aot.pricer.PriceLadderConsumer;
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
import com.telamin.fluxtion.runtime.event.Event;
import com.telamin.fluxtion.runtime.input.EventFeed;
import com.telamin.fluxtion.runtime.input.SubscriptionManager;
import com.telamin.fluxtion.runtime.input.SubscriptionManagerNode;
import com.telamin.fluxtion.runtime.lifecycle.BatchHandler;
import com.telamin.fluxtion.runtime.lifecycle.Lifecycle;
import com.telamin.fluxtion.runtime.node.MutableDataFlowContext;
import com.telamin.fluxtion.runtime.service.ServiceListener;
import com.telamin.fluxtion.runtime.service.ServiceRegistryNode;
import com.telamin.fluxtion.runtime.time.Clock;
import com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent;

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
 * <p>
 * Event classes supported:
 *
 * <ul>
 *   <li>com.telamin.fluxtion.builder.generation.model.ExportFunctionMarker
 *   <li>com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PriceLadderProcessor
        implements CloneableDataFlow<PriceLadderProcessor>,
        /*--- @ExportService start ---*/
        @ExportService PriceCalculator,
        @ExportService PriceLadderConsumer,
        @ExportService ServiceListener,
        /*--- @ExportService end ---*/
        DataFlow,
        InternalEventProcessor,
        BatchHandler {

    //Node declarations
    private final transient CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
    public final transient Clock clock = new Clock();
    private final transient MidCalculator midCalculator_3 = new MidCalculator();
    public final transient NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
    private final transient SkewCalculator skewCalculator_2 = new SkewCalculator(midCalculator_3);
    private final transient LevelsCalculator levelsCalculator_1 =
            new LevelsCalculator(skewCalculator_2);
    private final transient PriceLadderPublisher priceLadderPublisher_0 =
            new PriceLadderPublisher(levelsCalculator_1);
    private final transient SubscriptionManagerNode subscriptionManager =
            new SubscriptionManagerNode();
    private final transient MutableDataFlowContext context =
            new MutableDataFlowContext(
                    nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
    public final transient ServiceRegistryNode serviceRegistry = new ServiceRegistryNode();
    private final transient ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
    //Dirty flags
    private boolean initCalled = false;
    private boolean processing = false;
    private boolean buffering = false;
    private final transient IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
            new IdentityHashMap<>(4);
    private final transient IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
            new IdentityHashMap<>(4);

    private boolean isDirty_clock = false;
    private boolean isDirty_levelsCalculator_1 = false;
    private boolean isDirty_midCalculator_3 = false;
    private boolean isDirty_skewCalculator_2 = false;

    //Forked declarations

    //Filter constants

    //unknown event handler
    private Consumer unKnownEventHandler = (e) -> {
    };

    public PriceLadderProcessor(Map<Object, Object> contextMap) {
        if (context != null) {
            context.replaceMappings(contextMap);
        }
        context.setClock(clock);
        serviceRegistry.setDataFlowContext(context);
        //node auditors
        initialiseAuditor(clock);
        initialiseAuditor(nodeNameLookup);
        initialiseAuditor(serviceRegistry);
        if (subscriptionManager != null) {
            subscriptionManager.setSubscribingEventProcessor(this);
        }
        if (context != null) {
            context.setEventProcessorCallback(this);
        }
    }

    public PriceLadderProcessor() {
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
        if (event instanceof com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
            ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
            handleEvent(typedEvent);
        } else {
            unKnownEventHandler(event);
        }
    }

    public void handleEvent(ClockStrategyEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_clock = true;
        clock.setClockStrategy(typedEvent);
        afterEvent();
    }
    //EVENT DISPATCH - END

    //EXPORTED SERVICE FUNCTIONS - START
    @Override
    public boolean newPriceLadder(com.telamin.fluxtion.example.compile.aot.pricer.PriceLadder arg0) {
        beforeServiceCall(
                "public boolean com.telamin.fluxtion.example.compile.aot.node.MidCalculator.newPriceLadder(com.telamin.fluxtion.example.compile.aot.pricer.PriceLadder)");
        ExportFunctionAuditEvent typedEvent = functionAudit;
        isDirty_midCalculator_3 = midCalculator_3.newPriceLadder(arg0);
        if (guardCheck_skewCalculator_2()) {
            isDirty_skewCalculator_2 = skewCalculator_2.calculateSkewedLadder();
        }
        if (guardCheck_levelsCalculator_1()) {
            isDirty_levelsCalculator_1 = levelsCalculator_1.calculateLevelsForLadder();
        }
        if (guardCheck_priceLadderPublisher_0()) {
            priceLadderPublisher_0.publishPriceLadder();
        }
        afterServiceCall();
        return true;
    }

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

    @Override
    public void setLevels(int arg0) {
        beforeServiceCall(
                "public default void com.telamin.fluxtion.example.compile.aot.pricer.PriceCalculator.setLevels(int)");
        ExportFunctionAuditEvent typedEvent = functionAudit;
        isDirty_skewCalculator_2 = true;
        skewCalculator_2.setLevels(arg0);
        isDirty_levelsCalculator_1 = true;
        levelsCalculator_1.setLevels(arg0);
        priceLadderPublisher_0.setLevels(arg0);
        afterServiceCall();
    }

    @Override
    public void setPriceDistributor(
            com.telamin.fluxtion.example.compile.aot.node.PriceDistributor arg0) {
        beforeServiceCall(
                "public default void com.telamin.fluxtion.example.compile.aot.pricer.PriceCalculator.setPriceDistributor(com.telamin.fluxtion.example.compile.aot.node.PriceDistributor)");
        ExportFunctionAuditEvent typedEvent = functionAudit;
        isDirty_skewCalculator_2 = true;
        skewCalculator_2.setPriceDistributor(arg0);
        isDirty_levelsCalculator_1 = true;
        levelsCalculator_1.setPriceDistributor(arg0);
        priceLadderPublisher_0.setPriceDistributor(arg0);
        afterServiceCall();
    }

    @Override
    public void setSkew(int arg0) {
        beforeServiceCall(
                "public void com.telamin.fluxtion.example.compile.aot.node.SkewCalculator.setSkew(int)");
        ExportFunctionAuditEvent typedEvent = functionAudit;
        isDirty_skewCalculator_2 = true;
        skewCalculator_2.setSkew(arg0);
        isDirty_levelsCalculator_1 = true;
        levelsCalculator_1.setSkew(arg0);
        priceLadderPublisher_0.setSkew(arg0);
        afterServiceCall();
    }
    //EXPORTED SERVICE FUNCTIONS - END

    //EVENT BUFFERING - START
    public void bufferEvent(Object event) {
        buffering = true;
        if (event instanceof com.telamin.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
            ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
            auditEvent(typedEvent);
            isDirty_clock = true;
            clock.setClockStrategy(typedEvent);
        }
    }

    public void triggerCalculation() {
        buffering = false;
        String typedEvent = "No event information - buffered dispatch";
        if (guardCheck_skewCalculator_2()) {
            isDirty_skewCalculator_2 = true;
            skewCalculator_2.calculateSkewedLadder();
        }
        if (guardCheck_levelsCalculator_1()) {
            isDirty_levelsCalculator_1 = true;
            levelsCalculator_1.calculateLevelsForLadder();
        }
        if (guardCheck_priceLadderPublisher_0()) {
            priceLadderPublisher_0.publishPriceLadder();
        }
        afterEvent();
    }
    //EVENT BUFFERING - END

    private void auditEvent(Object typedEvent) {
        clock.eventReceived(typedEvent);
        nodeNameLookup.eventReceived(typedEvent);
        serviceRegistry.eventReceived(typedEvent);
    }

    private void auditEvent(Event typedEvent) {
        clock.eventReceived(typedEvent);
        nodeNameLookup.eventReceived(typedEvent);
        serviceRegistry.eventReceived(typedEvent);
    }

    private void initialiseAuditor(Auditor auditor) {
        auditor.init();
        auditor.nodeRegistered(levelsCalculator_1, "levelsCalculator_1");
        auditor.nodeRegistered(midCalculator_3, "midCalculator_3");
        auditor.nodeRegistered(priceLadderPublisher_0, "priceLadderPublisher_0");
        auditor.nodeRegistered(skewCalculator_2, "skewCalculator_2");
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
        nodeNameLookup.processingComplete();
        serviceRegistry.processingComplete();
        isDirty_clock = false;
        isDirty_levelsCalculator_1 = false;
        isDirty_midCalculator_3 = false;
        isDirty_skewCalculator_2 = false;
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
            dirtyFlagSupplierMap.put(clock, () -> isDirty_clock);
            dirtyFlagSupplierMap.put(levelsCalculator_1, () -> isDirty_levelsCalculator_1);
            dirtyFlagSupplierMap.put(midCalculator_3, () -> isDirty_midCalculator_3);
            dirtyFlagSupplierMap.put(skewCalculator_2, () -> isDirty_skewCalculator_2);
        }
        return dirtyFlagSupplierMap.getOrDefault(node, DataFlow.ALWAYS_FALSE);
    }

    @Override
    public void setDirty(Object node, boolean dirtyFlag) {
        if (dirtyFlagUpdateMap.isEmpty()) {
            dirtyFlagUpdateMap.put(clock, (b) -> isDirty_clock = b);
            dirtyFlagUpdateMap.put(levelsCalculator_1, (b) -> isDirty_levelsCalculator_1 = b);
            dirtyFlagUpdateMap.put(midCalculator_3, (b) -> isDirty_midCalculator_3 = b);
            dirtyFlagUpdateMap.put(skewCalculator_2, (b) -> isDirty_skewCalculator_2 = b);
        }
        dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
    }

    private boolean guardCheck_levelsCalculator_1() {
        return isDirty_skewCalculator_2;
    }

    private boolean guardCheck_priceLadderPublisher_0() {
        return isDirty_levelsCalculator_1;
    }

    private boolean guardCheck_skewCalculator_2() {
        return isDirty_midCalculator_3;
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
    public PriceLadderProcessor newInstance() {
        return new PriceLadderProcessor();
    }

    @Override
    public PriceLadderProcessor newInstance(Map<Object, Object> contextMap) {
        return new PriceLadderProcessor();
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
