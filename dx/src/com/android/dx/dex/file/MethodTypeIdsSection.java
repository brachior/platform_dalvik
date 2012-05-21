package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstMethodType;

import java.util.Collection;
import java.util.TreeMap;

/**
 * MethodType refs list section of a {@code .dex} file.
 */
public class MethodTypeIdsSection extends UniformItemSection {
	/**
	 * {@code non-null;} map from methodtype to {@link MethodTypeIdItem} instances
	 */
	private final TreeMap<CstMethodType, MethodTypeIdItem> methotypeIds;

	public MethodTypeIdsSection(DexFile file) {
		super("methodtype_ids", file, 4);
		this.methotypeIds = new TreeMap<>();
	}

    /** {@inheritDoc} */
	@Override
	public IndexedItem get(Constant cst) {
		return methotypeIds.get(cst);
	}

    /** {@inheritDoc} */
	@Override
	protected void orderItems() {
		int idx = 0;

		for (Object i : items()) {
			((MethodTypeIdItem) i).setIndex(idx);
			idx++;
		}
	}

    /** {@inheritDoc} */
	@Override
	public Collection<? extends Item> items() {
		return methotypeIds.values();
	}

    public MethodTypeIdItem intern(CstMethodType methodType) {
        if (methodType == null) {
            throw new NullPointerException("methodHandle == null");
        }

        throwIfPrepared();

        MethodTypeIdItem result = methotypeIds.get(methodType);

        if (result == null) {
            result = new MethodTypeIdItem(methodType);
            methotypeIds.put(methodType, result);
        }

        return result;
    }


    /**
     * Gets the index of the given methodType, which must have
     * been added to this instance.
     *
     * @param methodType {@code non-null;} the methodType to look up
     * @return {@code >= 0;} the reference's index
     */
    public int indexOf(CstMethodType methodType) {
        if (methodType == null) {
            throw new NullPointerException("methodType == null");
        }

        throwIfNotPrepared();

        MethodTypeIdItem item = methotypeIds.get(methodType);

        if (item == null) {
            throw new IllegalArgumentException("not found: " + methodType);
        }

        return item.getIndex();
    }
}
