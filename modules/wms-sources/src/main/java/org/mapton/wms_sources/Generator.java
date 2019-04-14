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
package org.mapton.wms_sources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Patrik Karlström
 */
public class Generator {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    protected final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .serializeNulls()
            .setVersion(1.0)
            .setPrettyPrinting()
            .setDateFormat(DATE_FORMAT)
            .create();
}
