/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.leoquintgames.capitalguess;

/**
 * A list of all the demos we have available.
 */
public final class DemoDetailsList {

    /** This class should not be instantiated. */
    private DemoDetailsList() {
    }

    public static final DemoDetails[] DEMOS = {
            new DemoDetails(R.string.capital_guess_label,
                    R.string.capital_guess_description,
                    CapitalGuessActivity.class)
    };
}
