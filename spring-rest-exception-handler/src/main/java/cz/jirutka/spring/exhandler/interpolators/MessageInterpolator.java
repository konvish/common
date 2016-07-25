/*
 * Copyright 2014 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.jirutka.spring.exhandler.interpolators;

import java.util.Map;

public interface MessageInterpolator {

    /**
     * Interpolates the message template using the given variables.
     *
     * @param messageTemplate The message to interpolate.
     * @param variables Map of variables that will be accessible for the template.
     * @return An interpolated message.
     */
    String interpolate(String messageTemplate, Map<String, Object> variables);
}
