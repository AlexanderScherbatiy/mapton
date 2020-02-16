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
package org.mapton.core_nb.tool.app;

import org.controlsfx.control.action.Action;
import org.mapton.api.MToolApp;
import static org.mapton.api.MToolApp.ICON_SIZE;
import org.openide.util.lookup.ServiceProvider;
import se.trixon.almond.nbp.Almond;
import se.trixon.almond.util.Dict;
import se.trixon.almond.util.fx.FxActionSwing;
import se.trixon.almond.util.icons.material.MaterialIcon;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = MToolApp.class)
public class NewsTool implements MToolApp {

    @Override
    public Action getAction() {
        FxActionSwing action = new FxActionSwing(Dict.NEWS.toString(), () -> {
            Almond.openAndActivateTopComponent("NewsTopComponent");
        });
        action.setGraphic(MaterialIcon._Social.NOTIFICATIONS.getImageView(ICON_SIZE));

        return action;
    }

    @Override
    public String getParent() {
        return Dict.SYSTEM.toString();
    }

}
