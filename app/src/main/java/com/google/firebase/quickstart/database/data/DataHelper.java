package com.google.firebase.quickstart.database.data;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {

    private static final String COLORS_FILE_NAME = "colors.json";

    // private static List<ColorWrapper> sColorWrappers = new ArrayList<>();

    public static List<LocationSuggetion> sLocation =
            new ArrayList<>(Arrays.asList(
                    new LocationSuggetion("Azimpur,Dhaka"),
                    new LocationSuggetion("Dhanmondi,Dhaka"),
                    new LocationSuggetion("Gulshan,Dhaka"),
                    new LocationSuggetion("Mirpur,Dhaka"),
                    new LocationSuggetion("New Market,Dhaka"),
                    new LocationSuggetion("Farmget,Dhaka"),
                    new LocationSuggetion("Motijil,Dhaka"),
                    new LocationSuggetion("College Road,Mymensingh"),
                    new LocationSuggetion("Town Hall,Mymensingh"),
                    new LocationSuggetion("Jamalpur Town,Jamalpur"),
                    new LocationSuggetion("Banani,Dhaka"),
                    new LocationSuggetion("Kakrail,Dhaka"),
                    new LocationSuggetion("Mogbajar Space"),
                    new LocationSuggetion("Uttara,Dhaka"),
                    new LocationSuggetion("Beily Road,Dhaka"),
                    new LocationSuggetion("Mohakhali,Dhala")));

   /* public interface OnFindColorsListener {
        void onResults(List<ColorWrapper> results);
    }*/

    public interface OnFindSuggestionsListener {
        void onResults(List<LocationSuggetion> results);
    }

    public static List<LocationSuggetion> getHistory(Context context, int count) {

        List<LocationSuggetion> suggestionList = new ArrayList<>();
        LocationSuggetion temp;

        sLocation.get(0).setIsHistory(true);
        sLocation.get(1).setIsHistory(true);
        for (int i = 0; i < sLocation.size(); i++) {
            temp = sLocation.get(i);

            if(temp.getIsHistory()==true) {
                suggestionList.add(temp);
            }
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (LocationSuggetion colorSuggestion : sLocation) {
            colorSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<LocationSuggetion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (LocationSuggetion suggestion : sLocation) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<LocationSuggetion>() {
                    @Override
                    public int compare(LocationSuggetion lhs, LocationSuggetion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<LocationSuggetion>) results.values);
                }
            }
        }.filter(query);

    }

/*
    public static void findColors(Context context, String query, final OnFindColorsListener listener) {
        initColorWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<ColorWrapper> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (ColorWrapper color : sColorWrappers) {
                        if (color.getName().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(color);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<ColorWrapper>) results.values);
                }
            }
        }.filter(query);

    }
*/
   /* private static void initColorWrapperList(Context context) {

        if (sColorWrappers.isEmpty()) {
            String jsonString = loadJson(context);
            sColorWrappers = deserializeColors(jsonString);
        }
    }

    private static String loadJson(Context context) {

        String jsonString;

        try {
            InputStream is = context.getAssets().open(COLORS_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonString;
    }

    private static List<ColorWrapper> deserializeColors(String jsonString) {

        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<ColorWrapper>>() {
        }.getType();
        return gson.fromJson(jsonString, collectionType);
    }*/


}