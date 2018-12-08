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
package org.mapton.ww_grid;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import org.mapton.api.MDict;
import org.mapton.worldwind.api.LayerBundle;
import org.mapton.worldwind.api.LayerBundleManager;
import static org.mapton.ww_grid.Options.*;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = LayerBundle.class)
public class GridController extends LayerBundle {

    private int mAltitudeMode;
    private BasicShapeAttributes mEquatorAttributes;
    private final BasicShapeAttributes mGridAttributes = new BasicShapeAttributes();
    private final RenderableLayer mLayer = new RenderableLayer();
    private final Options mOptions = Options.getInstance();
    private BasicShapeAttributes mPolarAttributes;
    private BasicShapeAttributes mTropicAttributes;

    public GridController() {
        mLayer.setName(MDict.GRID.toString());
        init();
    }

    @Override
    public void populate() throws Exception {
        getLayers().add(mLayer);
        initAttributes();
        refresh();

        mOptions.getPreferences().addPreferenceChangeListener((event) -> {
            refresh();
        });
    }

    private void drawLatitude(double latitude, BasicShapeAttributes attributes) {
        Position pos0 = Position.fromDegrees(latitude, 0);
        Position pos1 = Position.fromDegrees(latitude, 180);
        Position pos2 = Position.fromDegrees(latitude, -180);

        Path path1 = new Path(pos0, pos1);
        path1.setAttributes(attributes);
        path1.setFollowTerrain(true);
        path1.setAltitudeMode(mAltitudeMode);

        Path path2 = new Path(pos0, pos2);
        path2.setAttributes(attributes);
        path2.setFollowTerrain(true);
        path2.setAltitudeMode(mAltitudeMode);

        mLayer.addRenderable(path1);
        mLayer.addRenderable(path2);
    }

    private void drawLongitude(double longitude, BasicShapeAttributes attributes) {
        Position pos0 = Position.fromDegrees(0, longitude);
        Position pos1 = Position.fromDegrees(90, longitude);
        Position pos2 = Position.fromDegrees(-90, longitude);

        Path path1 = new Path(pos0, pos1);
        path1.setAttributes(attributes);
        path1.setFollowTerrain(true);
        path1.setAltitudeMode(mAltitudeMode);

        Path path2 = new Path(pos0, pos2);
        path2.setAttributes(attributes);
        path2.setFollowTerrain(true);
        path2.setAltitudeMode(mAltitudeMode);

        mLayer.addRenderable(path1);
        mLayer.addRenderable(path2);
    }

    private void init() {
    }

    private void initAttributes() {
        mGridAttributes.setOutlineMaterial(Material.CYAN);
        mGridAttributes.setOutlineWidth(0.7);
        mGridAttributes.setDrawOutline(true);
        mGridAttributes.setOutlineOpacity(0.5);

        mEquatorAttributes = (BasicShapeAttributes) mGridAttributes.copy();
        mEquatorAttributes.setOutlineMaterial(Material.RED);
        mEquatorAttributes.setOutlineWidth(3.0);

        mTropicAttributes = (BasicShapeAttributes) mGridAttributes.copy();
        mTropicAttributes.setOutlineMaterial(Material.ORANGE);
        mTropicAttributes.setOutlineWidth(2.0);

        mPolarAttributes = (BasicShapeAttributes) mGridAttributes.copy();
        mPolarAttributes.setOutlineMaterial(Material.BLUE);
        mPolarAttributes.setOutlineWidth(2.0);
    }

    private void plotGlobal() {
        if (mOptions.is(KEY_GLOBAL_LATITUDES)) {
            for (int i = -90; i < 90; i += 15) {
                drawLatitude(i, mGridAttributes);
            }
        }

        if (mOptions.is(KEY_GLOBAL_LONGITUDES)) {
            for (int i = -180; i < 180; i += 15) {
                drawLongitude(i, i == 0 ? mEquatorAttributes : mGridAttributes);
            }
        }

        final double POLAR = 66.563167; //As of 2018-11-23
        final double TROPIC = 23.43683; //As of 2018-11-23

        if (mOptions.is(KEY_GLOBAL_POLAR_ARCTIC)) {
            drawLatitude(POLAR, mPolarAttributes);
        }

        if (mOptions.is(KEY_GLOBAL_TROPIC_CANCER)) {
            drawLatitude(TROPIC, mTropicAttributes);
        }

        if (mOptions.is(KEY_GLOBAL_EQUATOR)) {
            drawLatitude(0.0, mEquatorAttributes);
        }

        if (mOptions.is(KEY_GLOBAL_TROPIC_CAPRICORN)) {
            drawLatitude(-TROPIC, mTropicAttributes);
        }

        if (mOptions.is(KEY_GLOBAL_POLAR_ANTARCTIC)) {
            drawLatitude(-POLAR, mPolarAttributes);
        }
    }

    private void plotLocal() {
    }

    private void refresh() {
        mLayer.removeAllRenderables();
        mAltitudeMode = mOptions.is(KEY_GLOBAL_CLAMP_TO_GROUND) ? WorldWind.CLAMP_TO_GROUND : WorldWind.ABSOLUTE;

        if (mOptions.is(KEY_GLOBAL_PLOT)) {
            plotGlobal();
        }

        if (mOptions.is(KEY_LOCAL_PLOT)) {
            plotLocal();
        }

        LayerBundleManager.getInstance().redraw();
    }
}
