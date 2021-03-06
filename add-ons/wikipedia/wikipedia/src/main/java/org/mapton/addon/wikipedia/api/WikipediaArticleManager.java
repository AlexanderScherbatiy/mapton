/* 
 * Copyright 2019 Patrik Karlström.
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
package org.mapton.addon.wikipedia.api;

import java.util.Locale;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Patrik Karlström
 */
public class WikipediaArticleManager {

    private ObjectProperty<ObservableList<WikipediaArticle>> mItems = new SimpleObjectProperty<>();
    private Locale mLocale = Locale.getDefault();

    public static WikipediaArticleManager getInstance() {
        return Holder.INSTANCE;
    }

    private WikipediaArticleManager() {
        mItems.setValue(FXCollections.observableArrayList());
    }

    public final ObservableList<WikipediaArticle> getItems() {
        return mItems == null ? null : mItems.get();
    }

    public Locale getLocale() {
        return mLocale;
    }

    public final ObjectProperty<ObservableList<WikipediaArticle>> itemsProperty() {
        if (mItems == null) {
            mItems = new SimpleObjectProperty<>(this, "items");
        }

        return mItems;
    }

    public void setLocale(Locale locale) {
        mLocale = locale;
    }

    private static class Holder {

        private static final WikipediaArticleManager INSTANCE = new WikipediaArticleManager();
    }
}
