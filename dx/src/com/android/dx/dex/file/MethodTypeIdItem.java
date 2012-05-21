package com.android.dx.dex.file;

import com.android.dx.dex.SizeOf;
import com.android.dx.rop.cst.CstMethodType;
import com.android.dx.rop.cst.CstString;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public class MethodTypeIdItem extends IndexedItem {

    private final CstMethodType cstMethodType;

    public MethodTypeIdItem(CstMethodType cstMethodType) {
        this.cstMethodType = cstMethodType;
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_METHODTYPE_ID_ITEM;
    }

    @Override
    public int writeSize() {
        return SizeOf.METHODTYPE_ITEM;
    }

    @Override
    public void addContents(DexFile file) {
        StringIdsSection stringIds = file.getStringIds();
        stringIds.intern(cstMethodType.getDescriptor());
    }

    @Override
    public void writeTo(DexFile file, AnnotatedOutput out) {
        StringIdsSection stringIds = file.getStringIds();
        CstString descriptor = cstMethodType.getDescriptor();
        int descIdx = stringIds.indexOf(descriptor);
        if (out.annotates()) {
            out.annotate(0, indexString() + ' ' + descriptor.toHuman());
            out.annotate(2, "  descriptor_idx: " + Hex.u2(descIdx));
        }
        out.writeShort(descIdx);
    }

}
