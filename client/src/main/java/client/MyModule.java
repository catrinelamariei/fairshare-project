/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.AdminPageCtrl;
import client.scenes.EventPageCtrl;
import client.scenes.PrivCheckPageCtrl;
import client.scenes.StartPageCtrl;
import client.utils.ServerUtils;
import client.utils.UndoService;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class MyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EventPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(PrivCheckPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AdminPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EventPageCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(UndoService.class).in(Scopes.SINGLETON);
        binder.install(new FactoryModuleBuilder()
            .build(EventPageCtrl.UndoServiceFactory.class));
    }
}