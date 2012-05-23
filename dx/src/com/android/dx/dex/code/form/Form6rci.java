/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.dx.dex.code.form;

import com.android.dx.dex.code.CstIndyInsn;
import com.android.dx.dex.code.DalvInsn;
import com.android.dx.dex.code.InsnFormat;
import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstLiteralBits;
import com.android.dx.util.AnnotatedOutput;

/**
 * Instruction format {@code 5rc}. See the instruction format spec
 * for details.
 */
public final class Form6rci extends InsnFormat {
    /** {@code non-null;} unique instance of this class */
    public static final InsnFormat THE_ONE = new Form6rci();

    /**
     * Constructs an instance. This class is not publicly
     * instantiable. Use {@link #THE_ONE}.
     */
    private Form6rci() {
        // This space intentionally left blank.
    }

    /** {@inheritDoc} */
    @Override
    public String insnArgString(DalvInsn insn) {
        RegisterSpecList registers = insn.getRegisters();
        CstLiteralBits value = (CstLiteralBits) ((Constant)((CstIndyInsn) insn).getIndy());
        return regRangeString(registers) + ", " + literalBitsString(value) + ", " + cstString(insn);
    }

    /** {@inheritDoc} */
    @Override
    public String insnCommentString(DalvInsn insn, boolean noteIndices) {
        if (noteIndices) {
            return cstIndyComment(insn);
        } else {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public int codeSize() {
        return 5;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCompatible(DalvInsn insn) {
        if (! ALLOW_EXTENDED_OPCODES) {
            return false;
        }

        if (!(insn instanceof CstIndyInsn)) {
            return false;
        }

        CstIndyInsn ci = (CstIndyInsn) insn;

        RegisterSpecList regs = ci.getRegisters();
        int sz = regs.size();

        return (sz == 0) ||
            (isRegListSequential(regs) &&
             unsignedFitsInShort(regs.get(0).getReg()) &&
             unsignedFitsInShort(regs.getWordCount()));
    }

    /** {@inheritDoc} */
    @Override
    public void writeTo(AnnotatedOutput out, DalvInsn insn) {
        RegisterSpecList regs = insn.getRegisters();
        int cpi = ((CstIndyInsn) insn).getIndex();
        int firstReg = (regs.size() == 0) ? 0 : regs.get(0).getReg();
        int count = regs.getWordCount();

        write(out, opcodeUnit(insn), cpi, (short) count, (short) firstReg);
    }
}
