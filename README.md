# DEPRECATED

Don't use this. The Maps v3 SDK [handles markers](https://developers.google.com/maps/articles/toomanymarkers). That with a few other [cool utilities](http://googlegeodevelopers.blogspot.com/2014/02/marker-clustering-and-heatmaps-new.html) make this library obsolete!


# Clusterkraf

A clustering library for the Google Maps Android API v2.

If you're using the [Polaris v2](https://github.com/cyrilmottier/Polaris2) library, check out our [pleiades](https://github.com/twotoasters/clusterkraf/tree/pleiades) branch.


## Features

- Clustering based on pixel proximity, not grid membership
- Animated cluster transitions
- Supports Android v2.2 (Froyo) and higher

## Setup

### Gradle

If you are using Gradle just add the following to your build.gradle file:

```groovy
dependencies {
    compile 'com.twotoasters.clusterkraf:library:1.0.+'
}
```

### Maven

If you are using maven add the following to your pom file:
```xml
<dependency>
    <groupId>com.twotoasters.clusterkraf</groupId>
    <artifactId>library</artifactId>
    <version>1.0.2</version>
</dependency>
```

### Eclipse
It's easy to add Clusterkraf to your app. Add the Clusterkraf library folder as an Android project to your Eclipse/ADT workspace, and reference it from your app as a library project. Also, we assume you have a data object that holds latitude and longitude coordinates of each point you want plotted on the map similar to this:

```java
public class YourMapPointModel {
    public LatLng latLng;
    public YourMapPointModel(LatLng latLng) {
        this.latLng = latLng;
    }
    // encapsulation omitted for brevity
}
```

Clusterkraf provides an `InputPoint` class which holds a `LatLng` position and an `Object` tag. You just need to construct an `ArrayList<InputPoint>` object based on your model objects similar to this example. In this example, we provide the model as the `Object` tag for the `InputPoint` so that we can later pass them back to you in callbacks as the `ClusterPoint` object's pointsInCluster list; see `MarkerOptionsChooser`, `OnInfoWindowClickDownstreamListener`, and `OnMarkerClickDownstreamListener`.

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

For a more detailed example, take a look at the included sample app's source code. 

## Sample App

The sample app demonstrates Activity lifecycle, custom marker icons, click handling, and Clusterkraf's options. You can build it from source, or install it from https://play.google.com/store/apps/details?id=com.twotoasters.clusterkraf.sample.

### Building the Sample App

1. In your local checkout of the Clusterkraf git repo, do `git submodule init` and `git submodule update`.
2. Add sample/ as a new Android project from existing source.
3. Add sample/libs/ViewPagerIndicator as a new Android project from existing source.
4. Authorize com.twotoasters.clusterkraf.sample built with your key of choice to your Google Maps for Android v2 API account.
5. Create a new Values file `private_strings.xml` in sample/res/values/ and create a string named `maps_api_key` with your Google Maps for Android v2 API key.

## License

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
