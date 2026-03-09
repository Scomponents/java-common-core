/*
 *  Copyright (C) 2008-2026 Intechcore GmbH - All Rights Reserved
 *
 *  This file is part of SComponents project
 *
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *
 *  Proprietary and confidential
 *
 *  Written by Intechcore GmbH <info@intechcore.com>
 */

package com.intechcore.scomponents.common.core.utils.lack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

@DisplayName("EnumFlags")
class EnumFlagsTest {
    private enum TestFlags {
        RED,
        GREEN,
        BLUE,
        YELLOW
    }

    private EnumFlags<TestFlags> target;

    @BeforeEach
    void setUp() {
        this.target = new EnumFlags<>(TestFlags.class);
    }

    @Nested
    @DisplayName("constructor and createEmpty()")
    class ConstructorTests {
        @Test
        void Constructor_ValidEnumClass_CreatesEmptySet() {
            Assertions.assertTrue(EnumFlagsTest.this.target.allContained().isEmpty());
        }

        @Test
        void Constructor_NullEnumClass_ThrowsNullPointerException() {
            Assertions.assertThrows(NullPointerException.class, () -> new EnumFlags<>(null));
        }

        @Test
        void CreateEmpty_ExistingFlags_ReturnsIndependentEmptyInstance() {
            EnumFlagsTest.this.target.set(TestFlags.RED);
            EnumFlags<TestFlags> empty = EnumFlagsTest.this.target.createEmpty();

            Assertions.assertNotSame(EnumFlagsTest.this.target, empty);
            Assertions.assertTrue(empty.allContained().isEmpty());
            Assertions.assertEquals(1, EnumFlagsTest.this.target.allContained().size());
        }
    }

    @Nested
    @DisplayName("query methods: contains, containsAll, containsAny, allContained")
    class QueryTests {
        @BeforeEach
        void setUpWithSomeFlags() {
            EnumFlagsTest.this.target.set(TestFlags.RED, TestFlags.BLUE);
        }

        @Test
        void Contains_PresentFlag_ReturnsTrue() {
            Assertions.assertTrue(EnumFlagsTest.this.target.contains(TestFlags.RED));
        }

        @Test
        void Contains_AbsentFlag_ReturnsFalse() {
            Assertions.assertFalse(EnumFlagsTest.this.target.contains(TestFlags.GREEN));
        }

        @Test
        void Contains_NullFlag_DoesNotThrowAnyException() {
            Assertions.assertDoesNotThrow(() -> EnumFlagsTest.this.target.contains(null));
        }

        @Test
        void ContainsAll_AllFlagsPresent_ReturnsTrue() {
            Assertions.assertTrue(EnumFlagsTest.this.target.containsAll(TestFlags.RED, TestFlags.BLUE));
        }

        @Test
        void ContainsAll_SomeFlagsMissing_ReturnsFalse() {
            Assertions.assertFalse(EnumFlagsTest.this.target.containsAll(TestFlags.RED, TestFlags.GREEN));
        }

        @Test
        void ContainsAll_EmptyVarargs_ReturnsTrue() {
            Assertions.assertTrue(EnumFlagsTest.this.target.containsAll());
        }

        @Test
        void ContainsAll_ArrayContainsNull_DoesNotThrowAnyException() {
            Assertions.assertDoesNotThrow(() -> EnumFlagsTest.this.target.containsAll(TestFlags.RED, null));
        }

        @Test
        void ContainsAny_AtLeastOneFlagPresent_ReturnsTrue() {
            Assertions.assertTrue(EnumFlagsTest.this.target.containsAny(TestFlags.GREEN, TestFlags.BLUE));
        }

        @Test
        void ContainsAny_NoFlagsPresent_ReturnsFalse() {
            Assertions.assertFalse(EnumFlagsTest.this.target.containsAny(TestFlags.GREEN, TestFlags.YELLOW));
        }

        @Test
        void ContainsAny_EmptyVarargs_ReturnsFalse() {
            Assertions.assertFalse(EnumFlagsTest.this.target.containsAny());
        }

        @Test
        void ContainsAny_ArrayContainsNull_ThrowsNullPointerException() {
            Assertions.assertDoesNotThrow(() -> EnumFlagsTest.this.target.containsAny(TestFlags.RED, null));
        }

        @Test
        void AllContained_AfterModifyingCopy_OriginalRemainsUnchanged() {
            Set<TestFlags> copy = EnumFlagsTest.this.target.allContained();
            copy.add(TestFlags.YELLOW);

            Assertions.assertFalse(EnumFlagsTest.this.target.contains(TestFlags.YELLOW));
            Assertions.assertEquals(2, EnumFlagsTest.this.target.allContained().size()); // RED, BLUE
            Assertions.assertEquals(3, copy.size());                // RED, BLUE, YELLOW
        }
    }

    @Nested
    @DisplayName("mutation methods: set, reset, setAll")
    class MutationTests {
        @Test
        void Set_SingleFlag_AddsFlag() {
            EnumFlagsTest.this.target.set(TestFlags.RED);
            Assertions.assertTrue(EnumFlagsTest.this.target.contains(TestFlags.RED));
        }

        @Test
        void Set_DuplicateFlag_DoesNotChangeSize() {
            EnumFlagsTest.this.target.set(TestFlags.RED);
            EnumFlagsTest.this.target.set(TestFlags.RED); // should not change anything
            Assertions.assertEquals(1, EnumFlagsTest.this.target.allContained().size());
        }

        @Test
        void Set_SingleNullFlag_ThrowsNullPointerException() {
            Assertions.assertThrows(NullPointerException.class, () -> EnumFlagsTest.this.target.set((TestFlags) null));
        }

        @Test
        void Set_MultipleFlags_AddsAllFlags() {
            EnumFlagsTest.this.target.set(TestFlags.RED, TestFlags.GREEN);
            Assertions.assertTrue(EnumFlagsTest.this.target.containsAll(TestFlags.RED, TestFlags.GREEN));
            Assertions.assertEquals(2, EnumFlagsTest.this.target.allContained().size());
        }

        @Test
        void Set_MultipleFlagsWithDuplicates_AddsEachFlagOnce() {
            EnumFlagsTest.this.target.set(TestFlags.RED, TestFlags.RED);
            Assertions.assertEquals(1, EnumFlagsTest.this.target.allContained().size());
        }

        @Test
        void Set_VarargsWithNullElement_ThrowsNullPointerException() {
            Assertions.assertThrows(NullPointerException.class, () -> EnumFlagsTest.this.target.set(TestFlags.RED, null));
        }

        @Test
        void SetAll_AnyState_AddsAllEnumConstants() {
            EnumFlagsTest.this.target.setAll();
            Assertions.assertEquals(EnumSet.allOf(TestFlags.class), EnumFlagsTest.this.target.allContained());
        }

        @Test
        void SetAll_CalledTwice_ProducesSameResult() {
            EnumFlagsTest.this.target.setAll();
            EnumFlagsTest.this.target.setAll(); // should not change
            Assertions.assertEquals(EnumSet.allOf(TestFlags.class), EnumFlagsTest.this.target.allContained());
        }
    }
}

