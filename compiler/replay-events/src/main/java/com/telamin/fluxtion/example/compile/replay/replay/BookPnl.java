package com.telamin.fluxtion.example.compile.replay.replay;

import com.telamin.fluxtion.runtime.annotations.OnEventHandler;
import com.telamin.fluxtion.runtime.node.NamedNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BookPnl implements NamedNode {

    private final String bookName;
    private transient int pnl;

    @OnEventHandler(filterVariable = "bookName")
    public boolean pnlUpdate(PnlUpdate pnlUpdate) {
        pnl = pnlUpdate.getAmount();
        return true;
    }

    @Override
    public String getName() {
        return "bookPnl_" + bookName;
    }
}
