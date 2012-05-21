package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodHandle;

import java.util.Collection;
import java.util.TreeMap;

public class MethodHandleIdsSection extends UniformItemSection {

    private TreeMap<CstMethodHandle, MethodHandleIdItem> methodhandleIds;

    public MethodHandleIdsSection(DexFile file) {
        super("methodhandle_ids", file, 4);
        this.methodhandleIds = new TreeMap<>();
    }

    /**
     * Interns an methodHandle constant into this instance.
     *
     * @param methodHandle {@code non-null;} the reference to intern
     * @return {@code non-null;} the interned reference
     */
    public MethodHandleIdItem intern(CstMethodHandle methodHandle) {
        if (methodHandle == null) {
            throw new NullPointerException("methodHandle == null");
        }

        throwIfPrepared();

        MethodHandleIdItem result = methodhandleIds.get(methodHandle);

        if (result == null) {
            result = new MethodHandleIdItem(methodHandle);
            methodhandleIds.put(methodHandle, result);
        }

        return result;
    }

    @Override
    public IndexedItem get(Constant cst) {
        return methodhandleIds.get(cst);
    }

    @Override
    protected void orderItems() {
        int idx = 0;

        for (Object i : items()) {
            ((MethodHandleIdItem) i).setIndex(idx);
            idx++;
        }
    }

    /**
     * Gets the index of the given methodHandle, which must have
     * been added to this instance.
     *
     * @param methodHandle {@code non-null;} the methodHandle to look up
     * @return {@code >= 0;} the reference's index
     */
    public int indexOf(CstMethodHandle methodHandle) {
        if (methodHandle == null) {
            throw new NullPointerException("methodHandle == null");
        }

        throwIfNotPrepared();

        MethodHandleIdItem item = methodhandleIds.get(methodHandle);

        if (item == null) {
            throw new IllegalArgumentException("not found: " + methodHandle);
        }

        return item.getIndex();
    }

    @Override
    public Collection<? extends Item> items() {
        return methodhandleIds.values();
    }
}
