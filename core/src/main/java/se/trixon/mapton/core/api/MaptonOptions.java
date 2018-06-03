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
package se.trixon.mapton.core.api;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import java.util.prefs.Preferences;
import org.openide.util.Lookup;
import org.openide.util.NbPreferences;

/**
 *
 * @author Patrik Karlström
 */
public class MaptonOptions {

    public static final String KEY_MAP_ONLY = "map_only";
    public static final String KEY_MAP_STYLE = "map_style";
    public static final String KEY_MAP_TYPE = "map_type";
    public static final String KEY_PREFER_POPOVER = "prefer_popover";

    private static final boolean DEFAULT_FULL_SCREEN = false;
    private static final double DEFAULT_MAP_LAT = 57.661509;
    private static final double DEFAULT_MAP_LON = 11.999312;
    private static final boolean DEFAULT_MAP_ONLY = false;
    private static final String DEFAULT_MAP_STYLE = "standard";
    private static final int DEFAULT_MAP_ZOOM = 5;
    private static final boolean DEFAULT_PREFER_POPOVER = true;
    private static final String KEY_DISPLAY_BOOKMARK = "display_bookmark";
    private static final String KEY_FULL_SCREEN = "fullscreen";
    private static final String KEY_MAP_CENTER_LAT = "map_center_lat";
    private static final String KEY_MAP_CENTER_LON = "map_center_lon";
    private static final String KEY_MAP_COO_TRANS = "map_coo_trans";
    private static final String KEY_MAP_HOME_LAT = "map_home_lat";
    private static final String KEY_MAP_HOME_LON = "map_home_lon";
    private static final String KEY_MAP_HOME_ZOOM = "map_home_zoom";
    private static final String KEY_MAP_KEY = "map_key";
    private static final String KEY_MAP_ZOOM = "map_zoom";
    private final Preferences mPreferences = NbPreferences.forModule(MaptonOptions.class);

    public static MaptonOptions getInstance() {
        return Holder.INSTANCE;
    }

    private MaptonOptions() {
    }

    public LatLong getMapCenter() {
        return new LatLong(
                mPreferences.getDouble(KEY_MAP_CENTER_LAT, DEFAULT_MAP_LAT),
                mPreferences.getDouble(KEY_MAP_CENTER_LON, DEFAULT_MAP_LON));
    }

    public CooTransProvider getMapCooTrans() {
        for (CooTransProvider cooTrans : Lookup.getDefault().lookupAll(CooTransProvider.class)) {
            if (cooTrans.getName().equalsIgnoreCase(getMapCooTransName())) {
                return cooTrans;
            }
        }

        return null;
    }

    public String getMapCooTransName() {
        return mPreferences.get(KEY_MAP_COO_TRANS, "WGS 84 DMS");
    }

    public LatLong getMapHome() {
        return new LatLong(
                mPreferences.getDouble(KEY_MAP_HOME_LAT, DEFAULT_MAP_LAT),
                mPreferences.getDouble(KEY_MAP_HOME_LON, DEFAULT_MAP_LON));
    }

    public int getMapHomeZoom() {
        return mPreferences.getInt(KEY_MAP_HOME_ZOOM, DEFAULT_MAP_ZOOM);
    }

    public String getMapKey() {
        return mPreferences.get(KEY_MAP_KEY, "AIzaSyCdVPck8GWP2piXLjl7XTf4QOaydWWYzFE");
    }

    public String getMapStyle() {
        return mPreferences.get(KEY_MAP_STYLE, DEFAULT_MAP_STYLE);
    }

    public MapTypeIdEnum getMapType() {
        switch (mPreferences.get(KEY_MAP_TYPE, "roadmap")) {
            case "HYBRID":
                return MapTypeIdEnum.HYBRID;

            case "SATELLITE":
                return MapTypeIdEnum.SATELLITE;

            case "TERRAIN":
                return MapTypeIdEnum.TERRAIN;

            default:
                return MapTypeIdEnum.ROADMAP;
        }
    }

    public int getMapZoom() {
        return mPreferences.getInt(KEY_MAP_ZOOM, DEFAULT_MAP_ZOOM);
    }

    public Preferences getPreferences() {
        return mPreferences;
    }

    public boolean isBookmarkVisible() {
        return mPreferences.getBoolean(KEY_DISPLAY_BOOKMARK, true);
    }

    public boolean isFullscreen() {
        return mPreferences.getBoolean(KEY_FULL_SCREEN, DEFAULT_FULL_SCREEN);
    }

    public boolean isMapOnly() {
        return mPreferences.getBoolean(KEY_MAP_ONLY, DEFAULT_MAP_ONLY);
    }

    public boolean isPreferPopover() {
        return mPreferences.getBoolean(KEY_PREFER_POPOVER, DEFAULT_PREFER_POPOVER);
    }

    public void setBookmarkVisible(boolean value) {
        mPreferences.putBoolean(KEY_DISPLAY_BOOKMARK, value);
    }

    public void setFullscreen(boolean value) {
        mPreferences.putBoolean(KEY_FULL_SCREEN, value);
    }

    public void setMapCenter(LatLong value) {
        mPreferences.putDouble(KEY_MAP_CENTER_LAT, value.getLatitude());
        mPreferences.putDouble(KEY_MAP_CENTER_LON, value.getLongitude());
    }

    public void setMapCooTrans(String value) {
        mPreferences.put(KEY_MAP_COO_TRANS, value);
    }

    public void setMapHome(LatLong value) {
        mPreferences.putDouble(KEY_MAP_HOME_LAT, value.getLatitude());
        mPreferences.putDouble(KEY_MAP_HOME_LON, value.getLongitude());
    }

    public void setMapHomeZoom(int value) {
        mPreferences.putInt(KEY_MAP_HOME_ZOOM, value);
    }

    public void setMapKey(String value) {
        mPreferences.put(KEY_MAP_KEY, value);
    }

    public void setMapOnly(boolean value) {
        mPreferences.putBoolean(KEY_MAP_ONLY, value);
    }

    public void setMapStyle(String value) {
        mPreferences.put(KEY_MAP_STYLE, value);
    }

    public void setMapType(MapTypeIdEnum mapType) {
        mPreferences.put(KEY_MAP_TYPE, mapType.getName());
    }

    public void setMapZoom(int value) {
        mPreferences.putInt(KEY_MAP_ZOOM, value);
    }

    public void setPreferPopover(boolean value) {
        mPreferences.putBoolean(KEY_PREFER_POPOVER, value);
    }

    private static class Holder {

        private static final MaptonOptions INSTANCE = new MaptonOptions();
    }
}
