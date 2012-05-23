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
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeList;

/**
 * Instruction which contains an explicit reference to a constant
 * and which might throw an exception.
 */
public final class ThrowingCstIndyInsn
        extends Insn {
    /** {@code non-null;} list of exceptions caught */
    private final TypeList catches;

    private final CstInvokeDynamic indy;
    private final CstInteger callsite;

    /**
     * Constructs an instance.
     *
     * @param opcode {@code non-null;} the opcode
     * @param position {@code non-null;} source position
     * @param sources {@code non-null;} specs for all the sources
     * @param catches {@code non-null;} list of exceptions caught
     * @param indy {@code non-null;} the constant
     */
    public ThrowingCstIndyInsn(Rop opcode, SourcePosition position,
                               RegisterSpecList sources,
                               TypeList catches, Constant indy, Constant callsite) {
        super(opcode, position, null, sources);

        if (!(indy instanceof CstInvokeDynamic)) {
            throw new RuntimeException(indy.toHuman() + " : is not an invoke-dynamic constant");
        }

        if (!(callsite instanceof CstInteger)) {
            throw new RuntimeException(callsite.toHuman() + " : is not an integer constant");
        }

        this.indy = (CstInvokeDynamic) indy;
        this.callsite = (CstInteger) callsite;

        if (opcode.getBranchingness() != Rop.BRANCH_THROW) {
            throw new IllegalArgumentException("bogus branchingness");
        }

        if (catches == null) {
            throw new NullPointerException("catches == null");
        }

        this.catches = catches;
    }

    /** {@inheritDoc} */
    @Override
    public String getInlineString() {
        return indy.toHuman() + " " + ThrowingInsn.toCatchString(catches);
    }

    /** {@inheritDoc} */
    @Override
    public TypeList getCatches() {
        return catches;
    }

    /** {@inheritDoc} */
    @Override
    public void accept(Visitor visitor) {
        visitor.visitThrowingCstIndyInsn(this);
    }

    /** {@inheritDoc} */
    @Override
    public Insn withAddedCatch(Type type) {
        return new ThrowingCstIndyInsn(getOpcode(), getPosition(),
                                   getSources(), catches.withAddedType(type),
                                   indy, callsite);
    }

    /** {@inheritDoc} */
    @Override
    public Insn withRegisterOffset(int delta) {
        return new ThrowingCstIndyInsn(getOpcode(), getPosition(),
                                   getSources().withOffset(delta),
                                   catches,
                                   indy, callsite);
    }

    /** {@inheritDoc} */
    @Override
    public Insn withNewRegisters(RegisterSpec result,
            RegisterSpecList sources) {

        return new ThrowingCstIndyInsn(getOpcode(), getPosition(),
                                   sources,
                                   catches,
                                   indy, callsite);
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
}
