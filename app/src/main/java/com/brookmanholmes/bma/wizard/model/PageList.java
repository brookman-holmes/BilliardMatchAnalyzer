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

import java.util.ArrayList;

/**
 * Represents a list of wizard pages.
 */
public class PageList extends ArrayList<Page> implements PageTreeNode {

    public PageList() {

    }

    public PageList(Page page) {
        add(page);
    }

    public PageList(Page page, Page page2) {
        add(page);
        add(page2);
    }

    public PageList(Page page, Page... pages) {
        add(page);
        for (Page page1 : pages) {
            add(page1);
        }
    }

    public PageList(Page... pages) {
        for (Page page : pages) {
            add(page);
        }
    }

    @Override
    public Page findByKey(String key) {
        for (Page childPage : this) {
            Page found = childPage.findByKey(key);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    @Override
    public void flattenCurrentPageSequence(ArrayList<Page> dest) {
        for (Page childPage : this) {
            childPage.flattenCurrentPageSequence(dest);
        }
    }
}
