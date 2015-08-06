/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.sonar.server.computation.component;

import java.util.NoSuchElementException;

/**
 * A {@link Visitor} which provide access to a representation of the path from the root to the currently visited
 * Component. It also provides a way to have an object associated to each Component and access it and all of its
 * parent's.
 */
public interface PathAwareVisitor<T> extends Visitor {

  StackElementFactory<T> getFactory();

  void visitProject(Component project, Path<T> path);

  void visitModule(Component module, Path<T> path);

  void visitDirectory(Component directory, Path<T> path);

  void visitFile(Component file, Path<T> path);

  void visitUnknown(Component unknownComponent, Path<T> path);

  void visitAny(Component component, Path<T> path);

  interface StackElementFactory<T> {
    T createForProject(Component project);

    T createForModule(Component module);

    T createForDirectory(Component directory);

    T createForFile(Component file);

    T createForUnknown(Component file);
  }

  interface Path<T> {
    /**
     * The stacked element of the current Component.
     */
    T current();

    /**
     * Tells whether the current Component is the root of the tree.
     */
    boolean isRoot();

    /**
     * The stacked element of the parent of the current Component.
     *
     * @throws NoSuchElementException if the current Component is the root of the tree
     * @see #isRoot()
     */
    T parent();

    /**
     * The stacked element of the root of the tree.
     */
    T root();

    /**
     * The path to the current Component as an Iterable of {@link PathAwareVisitor.PathElement} which starts with
     * the {@link PathAwareVisitor.PathElement} of the current Component and ends with the
     * {@link PathAwareVisitor.PathElement} of the root of the tree.
     */
    Iterable<PathElement<T>> getCurrentPath();
  }

  interface PathElement<T> {
    /**
     * The Component on the path.
     */
    Component getComponent();

    /**
     * The stacked element for the Component of this PathElement.
     */
    T getElement();
  }
}
