/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dx.dex.file;

import com.android.dx.cf.code.BootstrapArgumentList;
import com.android.dx.rop.cst.*;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.Hex;

/**
 * Representation of a list of class references.
 */
public final class BootstrapArgumentListItem extends OffsettedItem {
    public static final char CLASS_SECTION = 'L';
    public static final char STRING_SECTION = 'S';
    public static final char METHODTYPE_SECTION = 'T';
    public static final char METHODHANDLE_SECTION = 'H';
    public static final char INTEGER_SECTION = 'I';
    public static final char FLOAT_SECTION = 'F';
    public static final char LONG_SECTION = 'J';
    public static final char DOUBLE_SECTION = 'D';

    private final BootstrapArgumentList list;

    private static final int HEADER_SIZE = 4;

    /**
     * Constructs an instance.
     *
     * @param list {@code non-null;} the actual list
     */
    public BootstrapArgumentListItem(BootstrapArgumentList list) {
        super(1, -1); // -1 if the size of this instance when written is not immediately known
        this.list = list;
        int size = 0;
        for (int i = 0; i < list.size(); ++i) {
            TypedConstant constant = list.getTypedConstant(i);
            if (constant instanceof CstType) {
                size += 3;
            } else if (constant instanceof CstString) {
                size += 3;
            } else if (constant instanceof CstMethodType) {
                size += 3;
            } else if (constant instanceof CstMethodHandle) {
                size += 3;
            } else if (constant instanceof CstInteger) {
                size += 5;
            } else if (constant instanceof CstFloat) {
                size += 5;
            } else if (constant instanceof CstLong) {
                size += 9;
            } else if (constant instanceof CstDouble) {
                size += 9;
            }
        }
        setWriteSize(size);
    }

    @Override
    public int hashCode() {
        int size = list.size();
        int hash = 0;

        for (int i = 0; i < size; i++) {
            hash = (hash * 31) + list.get(i).hashCode();
        }

        return hash;
    }

    @Override
    public String toHuman() {
        throw new RuntimeException("unsupported");
    }

    @Override
    protected void writeTo0(DexFile file, AnnotatedOutput out) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        MethodTypeIdsSection methodTypeIds = file.getMethodTypeIds();
        MethodHandleIdsSection methodHandleIds = file.getMethodHandleIds();

        int sz = list.size();

        if (out.annotates()) {
            out.annotate(0, offsetString() + " bsmArgs_list");
            out.annotate(HEADER_SIZE, "  size: " + Hex.u4(sz));
            for (int i = 0; i < sz; i++) {
                TypedConstant constant = list.getTypedConstant(i);
                if (constant instanceof CstType) {
                    out.annotate(3, "class: " + Hex.u2(typeIds.indexOf((CstType) constant)) + " // " + constant.toHuman());
                } else if (constant instanceof CstString) {
                    out.annotate(3, "string: " + Hex.u2(stringIds.indexOf((CstString) constant)) + " // " + constant.toHuman());
                } else if (constant instanceof CstMethodType) {
                    out.annotate(3, "methodType: " + Hex.u2(methodTypeIds.indexOf((CstMethodType) constant)) + " // " + constant.toHuman());
                } else if (constant instanceof CstMethodHandle) {
                    out.annotate(3, "methodHandle: " + Hex.u2(methodHandleIds.indexOf((CstMethodHandle) constant)) + " // " + constant.toHuman());
                } else if (constant instanceof CstInteger) {
                    out.annotate(5, "int: " + constant.toHuman());
                } else if (constant instanceof CstFloat) {
                    out.annotate(5, "float: " + constant.toHuman());
                } else if (constant instanceof CstLong) {
                    out.annotate(9, "long: " + constant.toHuman());
                } else if (constant instanceof CstDouble) {
                    out.annotate(9, "double: " + constant.toHuman());
                }
            }
        }

        out.writeInt(sz);

        for (int i = 0; i < sz; i++) {
            TypedConstant constant = list.getTypedConstant(i);
            if (constant instanceof CstType) {
                out.writeByte(CLASS_SECTION);
                out.writeShort(typeIds.indexOf((CstType) constant));
            } else if (constant instanceof CstString) {
                out.writeByte(STRING_SECTION);
                out.writeInt(stringIds.indexOf((CstString) constant));
            } else if (constant instanceof CstMethodType) {
                out.writeByte(METHODTYPE_SECTION);
                out.writeShort(methodTypeIds.indexOf((CstMethodType) constant));
            } else if (constant instanceof CstMethodHandle) {
                out.writeByte(METHODHANDLE_SECTION);
                out.writeShort(methodHandleIds.indexOf((CstMethodHandle) constant));
            } else if (constant instanceof CstLiteral32) {
                if (constant instanceof CstInteger) {
                    out.writeByte(INTEGER_SECTION);
                } else if (constant instanceof CstFloat) {
                    out.writeByte(FLOAT_SECTION);
                }
                out.writeInt(((CstLiteralBits) constant).getIntBits());
            } else if (constant instanceof CstLiteral64) {
                if (constant instanceof CstLong) {
                    out.writeByte(LONG_SECTION);
                } else if (constant instanceof CstDouble) {
                    out.writeByte(DOUBLE_SECTION);
                }
                out.writeLong(((CstLiteralBits) constant).getLongBits());
            }
        }
    }

    @Override
    public ItemType itemType() {
        return ItemType.TYPE_BSMARGS_LIST_ITEM;
    }

    @Override
    public void addContents(DexFile file) {
        TypeIdsSection typeIds = file.getTypeIds();
        StringIdsSection stringIds = file.getStringIds();
        MethodTypeIdsSection methodTypeIds = file.getMethodTypeIds();
        MethodHandleIdsSection methodHandleIds = file.getMethodHandleIds();

        int sz = list.size();

        for (int i = 0; i < sz; i++) {
            TypedConstant constant = list.getTypedConstant(i);
            if (constant instanceof CstType) {
                typeIds.intern((CstType) constant);
            } else if (constant instanceof CstString) {
                stringIds.intern((CstString) constant);
            } else if (constant instanceof CstMethodType) {
                methodTypeIds.intern((CstMethodType) constant);
            } else if (constant instanceof CstMethodHandle) {
                methodHandleIds.intern((CstMethodHandle) constant);
            }
        }
    }
}
