package com.android.dx.rop.cst;

import com.android.dx.rop.type.Type;

/**
 * Constants of type {@code CONSTANT_MethodHandle_info}.
 */
public final class CstMethodHandle extends TypedConstant {

	private final int kind;

	/** {@code non-null;} the member reference */
	private final CstMemberRef memberRef;

	/**
	 * Constructs an instance.
	 * 
	 * @param kind
	 *            the kind of method handle
	 *            {@link java.lang.invoke.MethodHandle}
	 * @param memberRef
	 *            {@code non-null;} the member reference
	 */
	public CstMethodHandle(int kind, CstMemberRef memberRef) {
		if (kind < 1 || kind > 9) {
			throw new IllegalArgumentException("Illegal MethodHandle kind");
		}
		this.kind = kind;
		this.memberRef = memberRef;
	}

	/**
	 * Gets the kind.
	 * 
	 * @return {@code non-null;} the kind
	 */
	public int getKind() {
		return kind;
	}

	/**
	 * Gets the member reference.
	 * 
	 * @return {@code non-null;} the member reference
	 */
	public CstMemberRef getMemberRef() {
		return memberRef;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return (kind * 31) ^ memberRef.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CstMethodHandle) {
			CstMethodHandle other = (CstMethodHandle) obj;
			return (kind == other.kind) && (memberRef.equals(other.memberRef));
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "MH{" + toHuman() + '}';
	}

	/** {@inheritDoc} */
	@Override
	public String toHuman() {
		return kind + ":" + memberRef.toHuman();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCategory2() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public String typeName() {
		return "methodhandle";
	}

	/** {@inheritDoc} */
	@Override
	protected int compareTo0(Constant other) {
		CstMethodHandle otherMH = (CstMethodHandle) other;
		if (kind != otherMH.kind) {
			return kind - otherMH.kind;
		}
		return memberRef.compareTo(otherMH.memberRef);
	}

	/** {@inheritDoc} */
	@Override
	public Type getType() {
		return Type.METHODHANDLE;
	}
}
