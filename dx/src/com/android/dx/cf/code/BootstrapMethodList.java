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

package com.android.dx.cf.code;

import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.util.FixedSizeList;
import com.android.dx.util.MutabilityException;

public final class BootstrapMethodList extends FixedSizeList {
    /** {@code non-null;} zero-size instance */
    public static final BootstrapMethodList EMPTY = new BootstrapMethodList(0);

    /**
     * Constructs an instance.
     *
     * @param count the number of elements to be in the list
     */
    public BootstrapMethodList(int count) {
        super(count);
    }

    /**
     * Gets the indicated item.
     *
     * @param n {@code >= 0;} which item
     * @return {@code null-ok;} the indicated item
     */
    public BootstrapMethod get(int n) {
        return (BootstrapMethod) get0(n);
    }

    /**
     * Sets the item at the given index.
     *
     * @param n {@code >= 0, < size();} which element
     * @param bootstrapMethod {@code non-null;} the item
     */
    public void set(int n, BootstrapMethod bootstrapMethod) {
        if (bootstrapMethod == null) {
            throw new NullPointerException("bootstrapMethod == null");
        }

        set0(n, bootstrapMethod);
    }


    public static class BootstrapMethod {
        private final CstMethodHandle bootstrapMethodRef;
        private final BootstrapArgumentList bootstrapArguments;


        public BootstrapMethod(CstMethodHandle bootstrapMethodRef, BootstrapArgumentList bootstrapArguments) {
            if (bootstrapMethodRef == null) {
                throw new NullPointerException("bootstrapMethodRef == null");
            }
            if (bootstrapArguments == null) {
                throw new NullPointerException("bootstrapArguments == null");
            }
            if (bootstrapArguments.isMutable()) {
                throw new MutabilityException("bootstrapArguments.isMutable()");
            }

            this.bootstrapMethodRef = bootstrapMethodRef;
            this.bootstrapArguments = bootstrapArguments;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof BootstrapMethod)) {
                return false;
            }
            BootstrapMethod bsm = (BootstrapMethod) o;
            return bootstrapMethodRef.equals(bsm.bootstrapMethodRef) &&
                    bootstrapArguments.equals(bsm.bootstrapArguments);
        }

        @Override
        public int hashCode() {
            return (bootstrapMethodRef.hashCode() * 31) ^ bootstrapArguments.hashCode();
        }

        public CstMethodHandle getBootstrapMethodRef() {
            return bootstrapMethodRef;
        }

        public BootstrapArgumentList getBootstrapArguments() {
            return bootstrapArguments;
        }
    }
}
