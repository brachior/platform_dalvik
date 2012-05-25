package com.android.dx.dex.code;

import com.android.dx.rop.code.RegisterSpecList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstInvokeDynamic;

public class CstIndyInsn extends FixedSizeInsn {
    /**
     * {@code non-null;} the invoke-dynamic constant argument for this instruction
     */
    private final CstInvokeDynamic indy;

    /**
     * {@code non-null;} the integer constant argument for the callsite of this instruction
     */
    private int callSiteNumber;

    /**
     * {@code >= -1;} the constant pool index for {@link #indy}, or
     * {@code -1} if not yet set
     */
    private int index;

    /**
     * {@code >= -1;} the constant pool index for the class reference in
     * {@link #indy} if any, or {@code -1} if not yet set
     */
    private int classIndex;

    /**
     * Constructs an instance. The output address of this instance is initially
     * unknown ({@code -1}).
     * <p/>
     * <p><b>Note:</b> In the unlikely event that an instruction takes
     * absolutely no registers (e.g., a {@code nop} or a
     * no-argument no-result * static method call), then the given
     * register list may be passed as {@link
     * com.android.dx.rop.code.RegisterSpecList#EMPTY}.</p>
     *
     * @param opcode    the opcode; one of the constants from {@link Dops}
     * @param position  {@code non-null;} source position
     * @param registers {@code non-null;} register list, including a
     *                  result register if appropriate (that is, registers may be either
     *                  ins or outs)
     */
    public CstIndyInsn(Dop opcode, SourcePosition position, RegisterSpecList registers, Constant indy) {
        super(opcode, position, registers);

        if (!(indy instanceof CstInvokeDynamic)) {
            throw new RuntimeException(indy.toHuman() + " : is not an invoke-dynamic constant");
        }

        this.indy = (CstInvokeDynamic) indy;
        this.callSiteNumber = -1;
        this.index = -1;
        this.classIndex = -1;
    }

    @Override
    public DalvInsn withOpcode(Dop opcode) {
        CstIndyInsn result = new CstIndyInsn(opcode, getPosition(), getRegisters(), indy);

        if (callSiteNumber >= 0) {
            result.setCallSiteNumber(callSiteNumber);
        }

        if (index >= 0) {
            result.setIndex(index);
        }

        if (classIndex >= 0) {
            result.setClassIndex(classIndex);
        }

        return result;
    }

    @Override
    public DalvInsn withRegisters(RegisterSpecList registers) {
        CstIndyInsn result = new CstIndyInsn(getOpcode(), getPosition(), registers, indy);

        if (callSiteNumber >= 0) {
            result.setCallSiteNumber(callSiteNumber);
        }

        if (index >= 0) {
            result.setIndex(index);
        }

        if (classIndex >= 0) {
            result.setClassIndex(classIndex);
        }

        return result;
    }

    /**
     * Gets the invoke-dynamic constant argument.
     *
     * @return {@code non-null;} the invoke-dynamic constant argument
     */
    public CstInvokeDynamic getIndy() {
        return indy;
    }

    public int getCallSiteNumber() {
        return callSiteNumber;
    }

    public void setCallSiteNumber(int callsiteNumber) {
        this.callSiteNumber = callsiteNumber;
    }

    /**
     * Gets the constant's index. It is only valid to call this after
     * {@link #setIndex} has been called.
     *
     * @return {@code >= 0;} the constant pool index
     */
    public int getIndex() {
        if (index < 0) {
            throw new RuntimeException("index not yet set for " + indy);
        }

        return index;
    }

    /**
     * Returns whether the constant's index has been set for this instance.
     *
     * @return {@code true} iff the index has been set
     * @see #setIndex
     */
    public boolean hasIndex() {
        return (index >= 0);
    }

    /**
     * Sets the constant's index. It is only valid to call this method once
     * per instance.
     *
     * @param index {@code >= 0;} the constant pool index
     */
    public void setIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index < 0");
        }

        if (this.index >= 0) {
            throw new RuntimeException("index already set");
        }

        this.index = index;
    }

    /**
     * Gets the constant's class index. It is only valid to call this after
     * {@link #setClassIndex} has been called.
     *
     * @return {@code >= 0;} the constant's class's constant pool index
     */
    public int getClassIndex() {
        if (classIndex < 0) {
            throw new RuntimeException("class index not yet set");
        }

        return classIndex;
    }

    /**
     * Returns whether the constant's class index has been set for this
     * instance.
     *
     * @return {@code true} iff the index has been set
     * @see #setClassIndex
     */
    public boolean hasClassIndex() {
        return (classIndex >= 0);
    }

    /**
     * Sets the constant's class index. This is the constant pool index
     * for the class referred to by this instance's constant. Only
     * reference constants have a class, so it is only on instances
     * with reference constants that this method should ever be
     * called. It is only valid to call this method once per instance.
     *
     * @param index {@code >= 0;} the constant's class's constant pool index
     */
    public void setClassIndex(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index < 0");
        }

        if (this.classIndex >= 0) {
            throw new RuntimeException("class index already set");
        }

        this.classIndex = index;
    }

    @Override
    protected String argString() {
        return indy.toHuman() + ", " + callSiteNumber;
    }
}
