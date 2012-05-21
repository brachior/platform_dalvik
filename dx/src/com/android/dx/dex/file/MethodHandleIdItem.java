package com.android.dx.dex.file;

import com.android.dx.dex.SizeOf;
import com.android.dx.rop.cst.CstMemberRef;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public class MethodHandleIdItem extends IndexedItem {

    private final CstMethodHandle cstMethodHandle;

    public MethodHandleIdItem(CstMethodHandle cstMethodHandle) {
        this.cstMethodHandle = cstMethodHandle;
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_METHODHANDLE_ID_ITEM;
    }

    @Override
    public int writeSize() {
        return SizeOf.METHODHANDLE_ITEM;
    }

    @Override
    public void addContents(DexFile file) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        CstMemberRef memberRef = this.cstMethodHandle.getMemberRef();
        typeIds.intern(memberRef.getDefiningClass());
        stringIds.intern(memberRef.getNat().getName());
    }

    @Override
    public void writeTo(DexFile file, AnnotatedOutput out) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        CstMemberRef memberRef = this.cstMethodHandle.getMemberRef();
        int classIdx = typeIds.indexOf(memberRef.getDefiningClass());
        int nameIdx = stringIds.indexOf(memberRef.getNat().getName());
        int kind = this.cstMethodHandle.getKind();
        if (out.annotates()) {
            out.annotate(0, indexString() + ' ' + this.cstMethodHandle.toHuman());
            out.annotate(1, "  kind (" + Hex.u1(kind) + "): " + humanKind(kind));
            out.annotate(2, "  class_idx: " + Hex.u2(classIdx));
            out.annotate(4, "  name_idx:  " + Hex.u4(nameIdx));
        }
        out.writeByte(kind);
        out.writeShort(classIdx);
        out.writeInt(nameIdx);
    }

    private String humanKind(int kind) {
        switch (kind) {
            case 1:
                return "getField";
            case 2:
                return "getStatic";
            case 3:
                return "putField";
            case 4:
                return "putStatic";
            case 5:
                return "invokeVirtual";
            case 6:
                return "invokeStatic";
            case 7:
                return "invokeSpecial";
            case 8:
                return "newInvokeSpecial";
            case 9:
                return "invokeInterface";
        }
        return null;
    }
}
