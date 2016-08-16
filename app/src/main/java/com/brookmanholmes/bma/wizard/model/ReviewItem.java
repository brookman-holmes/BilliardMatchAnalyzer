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

/**
 * Represents a single line item on the final review page.
 *
 * @see com.brookmanholmes.bma.wizard.ui.ReviewFragment
 */
public class ReviewItem {
    public static final int DEFAULT_WEIGHT = 0;

    private int weight;
    private String title;
    private String displayValue;
    private String pageKey;

    public ReviewItem(String title, String displayValue, String pageKey) {
        this(title, displayValue, pageKey, DEFAULT_WEIGHT);
    }

    public ReviewItem(String title, String displayValue, String pageKey, int weight) {
        this.title = title;
        this.displayValue = displayValue;
        this.pageKey = pageKey;
        this.weight = weight;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getPageKey() {
        return pageKey;
    }

    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
