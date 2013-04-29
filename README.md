Clusterkraf
===========

A clustering library for the Google Maps Android API v2.

Features
========

- Clustering based on pixel proximity, not grid membership
- Animated cluster transitions
- Supports Android v2.2 (Froyo) and higher

Setup
=====

It's easy to add Clusterkraf to your app. We assume you have a data object that holds latitude and longitude coordinates of each point you want plotted on the map similar to this:

```java
public class YourMapPointModel {
    public LatLng latLng;
    public YourMapPointModel(LatLng latLng) {
        this.latLng = latLng;
    }
    // encapsulation omitted for brevity
}
```

Clusterkraf provides an `InputPoint` class which holds a `LatLng` position and an `Object` tag. You just need to construct an `ArrayList<InputPoint>` object based on your model objects similar to this example. In this example, we provide the model as the `Object` tag for the `InputPoint` so that we can later access it callbacks; see `MarkerOptionsChooser`, `OnInfoWindowClickDownstreamListener`, and `OnMarkerClickDownstreamListener`.

```java
public class YourActivity extends FragmentActivity {
    YourMapPointModel[] yourMapPointModels = new YourMapPointModel[] { new YourMapPointModel(new LatLng(0d, 1d) /* etc */ ) };
    ArrayList<InputPoint> inputPoints;
        
    private void buildInputPoints() {
        this.inputPoints = new ArrayList<InputPoint>(yourMapPointModels.length);
        for (YourMapPointModel model : this.yourMapPointModels) {
            this.inputPoints.add(new InputPoint(model.latLng, model));
        }
    }
}
```

When your `GoogleMap` is initialized and your `ArrayList<InputPoint>` is built, you can then initialize Clusterkraf.

```java
    // YourActivity

    Clusterkraf clusterkraf;

    private void initClusterkraf() {
        if (this.map != null && this.inputPoints != null && this.inputPoints.size() > 0) {
    		com.twotoasters.clusterkraf.Options options = new com.twotoasters.clusterkraf.Options();
    		// customize the options before you construct a Clusterkraf instance
    		this.clusterkraf = new Clusterkraf(this.map, this.options, this.inputPoints);
    	}
    }
```

You've added a really sweet clustered map to your Android app.

For a more detailed example, including Activity lifecycle, custom marker icons, click handling, and demonstration of Clusterkraf's options, take a look at the included sample app's source code. You can build it yourself by just adding your Google Maps for Android v2 API key, or you can download it from Google Play at https://play.google.com/store/apps/details?id=com.twotoasters.clusterkraf.sample.

License
=======

    Copyright 2013 Two Toasters

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
