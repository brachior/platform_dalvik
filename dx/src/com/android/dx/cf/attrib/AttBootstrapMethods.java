package com.android.dx.cf.attrib;

import com.android.dx.cf.code.BootstrapMethodList;
import com.android.dx.util.MutabilityException;


public class AttBootstrapMethods extends BaseAttribute {
    /**
     * {@code non-null;} attribute name for attributes of this type
     */
    public static final String ATTRIBUTE_NAME = "BootstrapMethods";

    /**
     * {@code non-null;} list of bootstrap method description
     */
    private final BootstrapMethodList bootstrapMethods;

    /**
     * Constructs an instance.
     */
    public AttBootstrapMethods(BootstrapMethodList bootstrapMethods) {
        super(ATTRIBUTE_NAME);

        try {
            if (bootstrapMethods.isMutable()) {
                throw new MutabilityException("bootstrapMethods.isMutable()");
            }
        } catch (NullPointerException ex) {
            // Translate the exception.
            throw new NullPointerException("bootstrapMethods == null");
        }

        this.bootstrapMethods = bootstrapMethods;
    }

    public BootstrapMethodList getBootstrapMethods() {
        return bootstrapMethods;
    }

    /**
     * {@inheritDoc}
     */
    public int byteLength() {
        int size = 8;
        for (int i = 0; i < bootstrapMethods.size(); ++i) {
            size += 4 + 2 * bootstrapMethods.get(i).getBootstrapArguments().size();
        }
        return size;
    }


}
