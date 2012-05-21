package com.android.dx.rop.cst;

import com.android.dx.rop.type.Type;

/**
 * Constants of type {@code CONSTANT_MethodType_info}.
 */
public final class CstMethodType extends TypedConstant {

	/** {@code non-null;} the descriptor (type) */
	private final CstString descriptor;

	/**
	 * Constructs an instance.
	 * 
	 * @param descriptor
	 *            {@code non-null;} the descriptor
	 */
	public CstMethodType(CstString descriptor) {
		if (descriptor == null) {
			throw new NullPointerException("descriptor == null");
		}
		this.descriptor = descriptor;
	}

	/**
	 * Gets the descriptor.
	 * 
	 * @return {@code non-null;} the descriptor
	 */
	public CstString getDescriptor() {
		return descriptor;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return descriptor.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CstMethodType)) {
			return false;
		}
		CstMethodType other = (CstMethodType) obj;
		return descriptor.equals(other.descriptor);
	}

	/**
	 * Returns an unadorned but human-readable version of the methodtype value.
	 * 
	 * @return {@code non-null;} the human form
	 */
	@Override
	public String toHuman() {
		return descriptor.toHuman();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCategory2() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String typeName() {
		return "methodtype";
	}

	/** {@inheritDoc} */
	@Override
	protected int compareTo0(Constant other) {
		CstMethodType otherNat = (CstMethodType) other;
		return descriptor.compareTo(otherNat.descriptor);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "MT{" + toHuman() + '}';
	}

	/** {@inheritDoc} */
	@Override
	public Type getType() {
		return Type.METHODTYPE;
	}
}
