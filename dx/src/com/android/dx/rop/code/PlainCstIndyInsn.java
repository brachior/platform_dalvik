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

package com.android.dx.rop.code;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInteger;
import com.android.dx.rop.cst.CstInvokeDynamic;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;

/**
 * Instruction which contains an explicit reference to a constant
 * but which cannot throw an exception.
 */
public final class PlainCstIndyInsn extends Insn {

    private final CstInvokeDynamic indy;
    private final CstInteger callsite;

    /**
     * Constructs an instance.
     *
     * @param opcode {@code non-null;} the opcode
     * @param position {@code non-null;} source position
     * @param result {@code null-ok;} spec for the result, if any
     * @param sources {@code non-null;} specs for all the sources
     * @param indy {@code non-null;} the invoke-dynamic constant
     * @param callsite {@code non-null;} the interger constant
     */
    public PlainCstIndyInsn(Rop opcode, SourcePosition position, RegisterSpec result, RegisterSpecList sources, Constant indy, Constant callsite) {
        super(opcode, position, result, sources);

        if (!(indy instanceof CstInvokeDynamic)) {
            throw new RuntimeException(indy.toHuman() + " : is not an invoke-dynamic constant");
        }

        if (!(callsite instanceof CstInteger)) {
            throw new RuntimeException(callsite.toHuman() + " : is not an integer constant");
        }

        this.indy = (CstInvokeDynamic) indy;
        this.callsite = (CstInteger) callsite;

        if (opcode.getBranchingness() != Rop.BRANCH_NONE) {
            throw new IllegalArgumentException("bogus branchingness");
        }
    }

    /**
     * Gets the invoke-dynamic constant argument.
     *
     * @return {@code non-null;} the invoke-dynamic constant argument
     */
    public CstInvokeDynamic getIndy() {
        return indy;
    }

    /**
     * Gets the callsite constant argument.
     *
     * @return {@code non-null;} the callsite constant argument
     */
    public CstInteger getCallsite() {
        return callsite;
    }

    /** {@inheritDoc} */
    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }

    /** {@inheritDoc} */
    @Override
    public void accept(Visitor visitor) {
        visitor.visitPlainCstIndyInsn(this);
    }

    /** {@inheritDoc} */
    @Override
    public Insn withAddedCatch(Type type) {
        throw new UnsupportedOperationException("unsupported");
    }

    /** {@inheritDoc} */
    @Override
    public Insn withRegisterOffset(int delta) {
        return new PlainCstIndyInsn(getOpcode(), getPosition(),
                                getResult().withOffset(delta),
                                getSources().withOffset(delta),
                                getIndy(), getCallsite());
    }

    /** {@inheritDoc} */
    @Override
    public Insn withNewRegisters(RegisterSpec result,
            RegisterSpecList sources) {

        return new PlainCstIndyInsn(getOpcode(), getPosition(),
                                result,
                                sources,
                                getIndy(), getCallsite());

    }
}
