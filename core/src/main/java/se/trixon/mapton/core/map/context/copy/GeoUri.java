/* 
 * Copyright 2018 Patrik Karlström.
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
package se.trixon.mapton.core.map.context.copy;

import java.util.Locale;
import org.openide.util.lookup.ServiceProvider;
import se.trixon.mapton.core.api.MapContextMenuProvider;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = MapContextMenuProvider.class)
public class GeoUri extends MapContextMenuProvider {

    @Override
    public String getName() {
        return "Geo URI";
    }

    @Override
    public ContextType getType() {
        return ContextType.COPY;
    }

    @Override
    public String getUrl() {
        return String.format(Locale.ENGLISH, "geo:%.6f,%.6f;crs=wgs84",
                getLatitude(),
                getLongitude()
        );
    }

}
