/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brookmanholmes.bma.wizard.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Represents a single page in the wizard.
 */
public abstract class Page implements PageTreeNode {
    /**
     * The key into {@link #getData()} used for wizards with simple (single) values.
     */
    public static final String SIMPLE_DATA_KEY = "_";
    private static final String TAG = "Page";
    protected final ModelCallbacks modelCallbacks;

    /**
     * Current wizard values/selections.
     */
    protected Bundle data = new Bundle();
    protected String title;
    protected String parentKey;
    private boolean required = false;

    protected Page(ModelCallbacks callbacks, String title) {
        modelCallbacks = callbacks;
        this.title = title;
    }

    public Bundle getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public boolean isRequired() {
        return required;
    }

    public Page setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public Page setParentKey(String parentKey) {
        this.parentKey = parentKey;
        return this;
    }

    @Override
    public Page findByKey(String key) {
        return getKey().equals(key) ? this : null;
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
        dest.add(this);
    }

    public abstract Fragment createFragment();

    public String getKey() {
        return (parentKey != null) ? parentKey + ":" + title : title;
    }

    public abstract void getReviewItems(ArrayList<ReviewItem> dest);

    public boolean isCompleted() {
        return true;
    }

    public void resetData(Bundle data) {
        this.data = data;
        notifyDataChanged();
    }

    public void notifyDataChanged() {
        modelCallbacks.onPageDataChanged(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (required != page.required) return false;
        if (!modelCallbacks.equals(page.modelCallbacks)) return false;
        if (!title.equals(page.title)) return false;
        return parentKey.equals(page.parentKey);

    }

    @Override
    public int hashCode() {
        int result = modelCallbacks.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + parentKey.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getKey();
    }
}
