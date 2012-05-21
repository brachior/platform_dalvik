package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInvokeDynamic;

import java.util.Collection;
import java.util.TreeMap;

public class InvokeDynamicIdsSection extends UniformItemSection {

    private final TreeMap<CstInvokeDynamic, InvokeDynamicIdItem> invokeDynamicIds;

    public InvokeDynamicIdsSection(DexFile file) {
        super("invokedynamic_id", file, 4);
        this.invokeDynamicIds = new TreeMap<>();
    }

    @Override
    public IndexedItem get(Constant cst) {
        return invokeDynamicIds.get(cst);
    }

    @Override
    protected void orderItems() {
        int idx = 0;

        for (Object i : items()) {
            ((InvokeDynamicIdItem) i).setIndex(idx);
            idx++;
        }
    }

    @Override
    public Collection<? extends Item> items() {
        return invokeDynamicIds.values();
    }

    /**
     * Interns an invokedynamic constant into this instance.
     *
     * @param invokeDynamic {@code non-null;} the reference to intern
     * @return {@code non-null;} the interned reference
     */
    public InvokeDynamicIdItem intern(CstInvokeDynamic invokeDynamic) {
        if (invokeDynamic == null) {
            throw new NullPointerException("invokeDynamic == null");
        }

        throwIfPrepared();

        InvokeDynamicIdItem result = invokeDynamicIds.get(invokeDynamic);

        if (result == null) {
            result = new InvokeDynamicIdItem(invokeDynamic);
            invokeDynamicIds.put(invokeDynamic, result);
        }

        return result;
    }

    /**
     * Gets the index of the given invokeDynamic, which must have
     * been added to this instance.
     *
     * @param invokeDynamic {@code non-null;} the invokeDynamic to look up
     * @return {@code >= 0;} the reference's index
     */
    public int indexOf(CstInvokeDynamic invokeDynamic) {
        if (invokeDynamic == null) {
            throw new NullPointerException("invokeDynamic == null");
        }

        throwIfNotPrepared();

        InvokeDynamicIdItem item = invokeDynamicIds.get(invokeDynamic);

        if (item == null) {
            throw new IllegalArgumentException("not found: " + invokeDynamic);
        }

        return item.getIndex();
    }
}
