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
package org.mapton.worldwind;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import java.awt.Color;
import javafx.collections.ListChangeListener;
import org.mapton.api.MKey;
import org.mapton.api.MWikipediaArticle;
import org.mapton.api.MWikipediaArticleManager;
import org.mapton.api.Mapton;
import org.mapton.worldwind.api.LayerBundle;
import org.mapton.worldwind.api.WWHelper;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Patrik Karlström
 */
@ServiceProvider(service = LayerBundle.class)
public class WikipediaLayerBundle extends LayerBundle {

    private final RenderableLayer mLayer = new RenderableLayer();
    private final MWikipediaArticleManager mWikipediaArticleManager = MWikipediaArticleManager.getInstance();

    public WikipediaLayerBundle() {
        mLayer.setName("Wikipedia");
        setCategoryAddOns(mLayer);
        setName("Wikipedia");

        init();
        initRepaint();
        initListeners();
    }

    @Override
    public void populate() throws Exception {
        getLayers().add(mLayer);
        repaint(0);
    }

    private void init() {
        mLayer.setPickEnabled(true);
        attachTopComponentToLayer("ObjectPropertiesTopComponent", mLayer);
    }

    private void initListeners() {
        mWikipediaArticleManager.getItems().addListener((ListChangeListener.Change<? extends MWikipediaArticle> c) -> {
            repaint();
            mLayer.setEnabled(true);
        });
    }

    private void initRepaint() {
        setPainter(() -> {
            mLayer.removeAllRenderables();

            for (MWikipediaArticle article : mWikipediaArticleManager.getItems()) {
                PointPlacemark placemark = new PointPlacemark(Position.fromDegrees(article.getLatLon().getLatitude(), article.getLatLon().getLongitude()));
                placemark.setLabelText(article.getTitle());
                placemark.setValue(AVKey.DISPLAY_NAME, article.getDescription());
                placemark.setLineEnabled(false);
                placemark.setEnableLabelPicking(true); // enable label picking for this placemark

                placemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
                placemark.setEnableLabelPicking(true);

                PointPlacemarkAttributes attrs = new PointPlacemarkAttributes(placemark.getDefaultAttributes());
                attrs.setImageAddress("org/mapton/wikipedia/Wikipedia-logo.png");
                attrs.setImageColor(Color.decode("#ff8888"));
                attrs.setLabelScale(1.0);
                attrs.setScale(0.15);
                attrs.setImageOffset(Offset.CENTER);

                placemark.setAttributes(attrs);
                placemark.setHighlightAttributes(WWHelper.createHighlightAttributes(attrs, 1.5));
                placemark.setValue(WWHelper.KEY_RUNNABLE_LEFT_CLICK, (Runnable) () -> {
                    Mapton.getGlobalState().put(MKey.WIKIPEDIA_ARTICLE, article);
                });

                mLayer.addRenderable(placemark);
            }
        });
    }
}