# Reference examples

This module is a cookbook of small, focused samples that each demonstrate one Fluxtion feature in isolation. Browse by
area (functional ops, group-by, windowing, triggers, nodes, event feeds) and open the corresponding Java file. All
samples are runnable from your IDE (look for a public static void main).

If you’re getting started, see the top‑level [getting-started](../getting-started) module first, then come back here to
find a specific pattern.

## Functional building blocks

- Map: [MapSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/MapSample.java)
- Filter: [FilterSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/FilterSample.java)
- FlatMap: [FlatMapSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/FlatMapSample.java)
- Merge: [MergeSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/MergeSample.java)
- Merge and map: [MergeAndMapSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/MergeAndMapSample.java)
- Default value: [DefaultValueSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/DefaultValueSample.java)
- Bi-map: [BiMapSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/BiMapSample.java)
- Re‑entrant events: [ReEntrantEventSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/ReEntrantEventSample.java)
- Subscribe to event: [SubscribeToEventSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/SubscribeToEventSample.java)
- Reset function: [ResetFunctionSample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/ResetFunctionSample.java)
- Sink: [SinkExample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/SinkExample.java)
- Get node by id: [GetFlowNodeByIdExample.java](src/main/java/com/telamin/fluxtion/example/reference/functional/GetFlowNodeByIdExample.java)

### Grouping and joins

- Basic groupBy: [GroupBySample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupBySample.java)
- Group by specific fields: [GroupByFieldsSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByFieldsSample.java)
- Group by map key: [GroupByMapKeySample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByMapKeySample.java)
- Group by map values: [GroupByMapValuesSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByMapValuesSample.java)
- Reduce grouped values: [GroupByReduceSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByReduceSample.java)
- To list: [GroupByToListSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByToListSample.java)
- To set: [GroupByToSetSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByToSetSample.java)
- Inner join: [GroupByJoinSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByJoinSample.java)
- Left outer join: [GroupByLeftOuterJoinSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByLeftOuterJoinSample.java)
- Right outer join: [GroupByRightOuterJoinSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByRightOuterJoinSample.java)
- Full outer join: [GroupByFullOuterJoinSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByFullOuterJoinSample.java)
- Multi‑join: [MultiJoinSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/MultiJoinSample.java)
- Sliding groupBy: [SlidingGroupBySample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/SlidingGroupBySample.java)
- Sliding groupBy (compound key): [SlidingGroupByCompoundKeySample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/SlidingGroupByCompoundKeySample.java)
- Tumbling groupBy: [TumblingGroupBySample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/TumblingGroupBySample.java)
- Tumbling groupBy (compound key): [TumblingGroupByCompoundKeySample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/TumblingGroupByCompoundKeySample.java)
- Delete group entries: [GroupByDeleteSample.java](src/main/java/com/telamin/fluxtion/example/reference/groupby/GroupByDeleteSample.java)

### Windowing and triggers

- Sliding window: [SlidingWindowSample.java](src/main/java/com/telamin/fluxtion/example/reference/windowing/SlidingWindowSample.java)
- Tumbling window: [TumblingWindowSample.java](src/main/java/com/telamin/fluxtion/example/reference/windowing/TumblingWindowSample.java)
- Tumbling window with trigger: [TumblingTriggerSample.java](src/main/java/com/telamin/fluxtion/example/reference/windowing/TumblingTriggerSample.java)

### Triggers

- Update trigger: [TriggerUpdateSample.java](src/main/java/com/telamin/fluxtion/example/reference/trigger/TriggerUpdateSample.java)
- Publish trigger: [TriggerPublishSample.java](src/main/java/com/telamin/fluxtion/example/reference/trigger/TriggerPublishSample.java)
- Reset trigger: [TriggerResetSample.java](src/main/java/com/telamin/fluxtion/example/reference/trigger/TriggerResetSample.java)
- Publish override: [TriggerPublishOverrideSample.java](src/main/java/com/telamin/fluxtion/example/reference/trigger/TriggerPublishOverrideSample.java)

### Working with nodes

- Wrap functions: [WrapFunctionsSample.java](src/main/java/com/telamin/fluxtion/example/reference/node/WrapFunctionsSample.java)
- Subscribe to node: [SubscribeToNodeSample.java](src/main/java/com/telamin/fluxtion/example/reference/node/SubscribeToNodeSample.java)
- Push pattern: [PushSample.java](src/main/java/com/telamin/fluxtion/example/reference/node/PushSample.java)
- Map from node property: [MapFromNodePropertySample.java](src/main/java/com/telamin/fluxtion/example/reference/node/MapNodeSupplierSample.java)
- Member variable supplier: [FlowSupplierAsMemberVariableSample.java](src/main/java/com/telamin/fluxtion/example/reference/node/FlowSupplierAsMemberVariableSample.java)

## Imperative node examples

### Advanced

- Batch support: [BatchSupport.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/advanced/BatchSupport.java)
- Collection support: [CollectionSupport.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/advanced/CollectionSupport.java)
- Fork/join support: [ForkJoinSupport.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/advanced/ForkJoinSupport.java)

### Callbacks

- After event callback: [AfterEventCallback.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/callback/AfterEventCallback.java)
- After trigger callback: [AfterTriggerCallback.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/callback/AfterTriggerCallback.java)
- Lifecycle callback: [LifecycleCallback.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/callback/LifecycleCallback.java)

### Filtering

- Static filtering: [StaticFiltering.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/filtering/StaticFiltering.java)
- Variable filtering: [VariableFiltering.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/filtering/VariableFiltering.java)

### Subscribe

- Multiple event types: [MultipleEventTypes.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/subscribe/MultipleEventTypes.java)
- Unknown event handling: [UnknownEventHandling.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/subscribe/UnknownEventHandling.java)
- Wrap node: [WrapNode.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/subscribe/WrapNode.java)

### Triggering

- Conditional trigger children: [ConditionalTriggerChildren.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggering/ConditionalTriggerChildren.java)
- Identify trigger parent: [IdentifyTriggerParent.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggering/IdentifyTriggerParent.java)
- Identify trigger parent by id: [IdentifyTriggerParentById.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggering/IdentifyTriggerParentById.java)
- Push trigger: [PushTrigger.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggering/PushTrigger.java)
- Trigger children: [TriggerChildren.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggering/TriggerChildren.java)

### Trigger overrides

- Invert dirty trigger: [InvertDirtyTrigger.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggeroverride/InvertDirtyTrigger.java)
- No-propagate handler: [NoPropagateHandler.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggeroverride/NoPropagateHandler.java)
- Single trigger override: [SingleTriggerOverride.java](src/main/java/com/telamin/fluxtion/example/reference/bindnode/triggeroverride/SingleTriggerOverride.java)

## Event feeds and runners

- Data flow runner (file feed): [DataFlowRunnerSample.java](src/main/java/com/telamin/fluxtion/example/reference/eventfeed/DataFlowRunnerSample.java)

---

Utilities used by several examples:

- MyFunctions: [MyFunctions.java](src/main/java/com/telamin/fluxtion/example/reference/MyFunctions.java)
