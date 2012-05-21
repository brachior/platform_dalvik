package com.android.dx.rop.cst;

import com.android.dx.cf.code.BootstrapMethodList;

/**
 * Constants of type {@code CONSTANT_InvokeDynamic_info}.
 */
public final class CstInvokeDynamic extends CstBaseMethodRef {

	/** the offset of bootstrap method arguments, see {@link com.android.dx.cf.code.BootstrapMethodList}*/
	private final long offsetBSMArgs;

    /** {@code null;} before being initialized when parsing bootstrap methods class attribute,
      * {@code non-null;} otherwise.
      */
    private BootstrapMethodList.BootstrapMethod bootstrapMethod;

    /**
     * Constructs an instance.
     *
     * @param offsetBSMArgs the offset of bootstrap method arguments
     * @param nat {@code non-null;} the name-and-type
     */
	public CstInvokeDynamic(long offsetBSMArgs, CstNat nat) {
        // CstInvokeDynamic has to be a CstBaseMethodRef so use Object
        // as a fake defining class
        super(CstType.OBJECT, nat);
		this.offsetBSMArgs = offsetBSMArgs;
	}

    /** {@inheritDoc} */
    @Override
    public final boolean equals(Object other) {
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        }

        CstInvokeDynamic otherIndy = (CstInvokeDynamic) other;
        return super.equals(other) &&
                bootstrapMethod.equals(otherIndy.bootstrapMethod);
    }

    /** {@inheritDoc} */
    @Override
    public final int hashCode() {
        return (super.hashCode() * 31) ^ bootstrapMethod.hashCode();
    }


    public BootstrapMethodList.BootstrapMethod getBootstrapMethod() {
        return bootstrapMethod;
    }

    public void setBootstrapMethod(BootstrapMethodList.BootstrapMethod bootstrapMethod) {
        this.bootstrapMethod = bootstrapMethod;
    }

    /**
	 * Gets the offset of bootstrap method arguments.
	 * 
	 * @return {@code non-null;} the offset
	 */
	public long getOffsetBSMArgs() {
		return offsetBSMArgs;
	}


	/** {@inheritDoc} */
	@Override
	public String toHuman() {
		return offsetBSMArgs + ":" + super.toHuman();
	}

	/** {@inheritDoc} */
	@Override
	public String typeName() {
		return "indy";
	}

	/** {@inheritDoc} */
	@Override
	protected int compareTo0(Constant other) {
		CstInvokeDynamic otherIndy = (CstInvokeDynamic) other;
		int cmp = (int) (offsetBSMArgs - otherIndy.offsetBSMArgs);
		if (cmp != 0) {
			return cmp;
		}
		return super.compareTo0(other);
	}
}
