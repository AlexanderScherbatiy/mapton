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
package se.trixon.mapton.core.tool;

import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.action.Action;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import se.trixon.almond.util.Dict;
import se.trixon.almond.util.fx.dialogs.SimpleDialog;
import se.trixon.mapton.api.MKmlCreator;
import se.trixon.mapton.api.MBookmarkManager;
import se.trixon.mapton.api.MTool;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = MTool.class)
public class ExportBookmarksToKmlTool implements MTool {

    private final ResourceBundle mBundle = NbBundle.getBundle(ExportBookmarksToKmlTool.class);
    private File mDestination;

    public ExportBookmarksToKmlTool() {
    }

    @Override
    public Action getAction() {
        Action action = new Action(mBundle.getString("export_to_kml"), (t) -> {
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Keyhole Markup Language (*.kml)", "*.kml");
            SimpleDialog.clearFilters();
            SimpleDialog.addFilter(new FileChooser.ExtensionFilter(Dict.ALL_FILES.toString(), "*"));
            SimpleDialog.addFilter(filter);
            SimpleDialog.setFilter(filter);
            SimpleDialog.setTitle(String.format("%s %s", Dict.SAVE.toString(), Dict.BOOKMARKS.toString().toLowerCase()));

            if (mDestination == null) {
                SimpleDialog.setPath(FileUtils.getUserDirectory());
            } else {
                SimpleDialog.setPath(mDestination.getParentFile());
                SimpleDialog.setSelectedFile(new File(""));
            }

            if (SimpleDialog.saveFile(new String[]{"kml"})) {
                mDestination = SimpleDialog.getPath();
                try {
                    new Exporter(mDestination);
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

        });

        return action;
    }

    @Override
    public String getParent() {
        return null;
    }

    public class Exporter extends MKmlCreator {

        private final TreeMap<String, Folder> mCategories = new TreeMap<>();
        private final MBookmarkManager mManager = MBookmarkManager.getInstance();

        public Exporter(File file) throws IOException {
            mDocument.setName(String.format("Mapton %s", Dict.BOOKMARKS.toString()));
            mManager.getItems().forEach((item) -> {
                Placemark placemark = KmlFactory.createPlacemark()
                        .withName(item.getName())
                        .withDescription(item.getDescription())
                        .withOpen(Boolean.TRUE);

                placemark.createAndSetPoint()
                        .addToCoordinates(item.getLongitude(), item.getLatitude());

                String key = StringUtils.defaultIfBlank(item.getCategory(), "---");
                mCategories.computeIfAbsent(key, k -> new Folder().withName(key)).addToFeature(placemark);
            });

            mCategories.values().forEach((folder) -> {
                mDocument.addToFeature(folder);
            });

            save(file);
        }
    }
}
