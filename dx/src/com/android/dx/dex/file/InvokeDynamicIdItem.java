package com.android.dx.dex.file;

import com.android.dx.cf.code.BootstrapArgumentList;
import com.android.dx.dex.SizeOf;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

public class InvokeDynamicIdItem extends IndexedItem {

    private final CstInvokeDynamic cstInvokeDynamic;
    private final BootstrapArgumentListItem bootstrapArguments;

    public InvokeDynamicIdItem(CstInvokeDynamic cstInvokeDynamic) {
        this.cstInvokeDynamic = cstInvokeDynamic;
        BootstrapArgumentList bootstrapArgumentsList = cstInvokeDynamic.getBootstrapMethod().getBootstrapArguments();
        this.bootstrapArguments = (bootstrapArgumentsList != null) ? new BootstrapArgumentListItem(bootstrapArgumentsList) : null;
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_INVOKEDYNAMIC_ID_ITEM;
    }

    @Override
    public int writeSize() {
        return SizeOf.INVOKEDYNAMIC_ITEM;
    }

    @Override
    public void addContents(DexFile file) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        MethodHandleIdsSection methodHandleIds = file.getMethodHandleIds();
        MixedItemSection bsmArgsLists = file.getBsmArgsLists();

        typeIds.intern(cstInvokeDynamic.getType());
        stringIds.intern(cstInvokeDynamic.getNat().getName());
        methodHandleIds.intern(cstInvokeDynamic.getBootstrapMethod().getBootstrapMethodRef());
        if (bootstrapArguments != null) {
            bsmArgsLists.intern(bootstrapArguments);
        }
    }

    @Override
    public void writeTo(DexFile file, AnnotatedOutput out) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        MethodHandleIdsSection methodHandleIds = file.getMethodHandleIds();

        int typeIdx = typeIds.indexOf(cstInvokeDynamic.getType());
        int protoIdx = stringIds.indexOf(cstInvokeDynamic.getNat().getName());
        int mhIdx = methodHandleIds.indexOf(cstInvokeDynamic.getBootstrapMethod().getBootstrapMethodRef());
        int offsetBSMArgs = OffsettedItem.getAbsoluteOffsetOr0(bootstrapArguments);

        if (out.annotates()) {
            StringBuilder sb = new StringBuilder();
            sb.append(cstInvokeDynamic.getBootstrapMethod().getBootstrapMethodRef().toHuman());
            sb.append(" bsmArgs(");

            BootstrapArgumentList bsmArgs = cstInvokeDynamic.getBootstrapMethod().getBootstrapArguments();
            int size = bsmArgs.size();

            for (int i = 0; i < size; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(bsmArgs.get(i).toHuman());
            }
            sb.append(")");

            out.annotate(0, indexString() + ' ' + sb.toString());
            out.annotate(4, "  type_idx:      " + Hex.u4(typeIdx) + " // " + cstInvokeDynamic.getType().toHuman());
            out.annotate(2, "  proto_idx: " + Hex.u2(protoIdx) + " // " + cstInvokeDynamic.getNat().toHuman());
            out.annotate(2, "  methodhandle_idx: " + Hex.u2(mhIdx) + " // " + cstInvokeDynamic.getBootstrapMethod().getBootstrapMethodRef().toHuman());
            out.annotate(8, "  bsmArgs_off:  " + Hex.u8(offsetBSMArgs));
        }

        out.writeInt(typeIdx);
        out.writeShort(protoIdx);
        out.writeShort(mhIdx);
        out.writeLong(offsetBSMArgs);
    }
}
