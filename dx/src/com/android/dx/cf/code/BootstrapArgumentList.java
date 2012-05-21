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

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.TypedConstant;
import com.android.dx.util.FixedSizeList;

public final class BootstrapArgumentList extends FixedSizeList {
    /**
     * {@code non-null;} zero-size instance
     */
    public static final BootstrapArgumentList EMPTY = new BootstrapArgumentList(0);

    /**
     * Constructs an instance.
     *
     * @param count the number of elements to be in the list
     */
    public BootstrapArgumentList(int count) {
        super(count);
    }

    /**
     * Gets the indicated item.
     *
     * @param n {@code >= 0;} which item
     * @return {@code null-ok;} the indicated item
     */
    public Constant get(int n) {
        return (Constant) get0(n);
    }

    /**
     * Gets the indicated item.
     *
     * @param n {@code >= 0;} which item
     * @return {@code null-ok;} the indicated item
     */
    public TypedConstant getTypedConstant(int n) {
        return (TypedConstant) get0(n);
    }

    /**
     * Sets the item at the given index.
     *
     * @param n                 {@code >= 0, < size();} which element
     * @param bootstrapArgument {@code non-null;} the item
     */
    public void set(int n, Constant bootstrapArgument) {
        if (bootstrapArgument == null) {
            throw new NullPointerException("bootstrapArgument == null");
        }

        set0(n, bootstrapArgument);
    }
}
