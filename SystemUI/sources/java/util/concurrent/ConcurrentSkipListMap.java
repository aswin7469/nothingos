package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConcurrentSkipListMap<K, V> extends AbstractMap<K, V> implements ConcurrentNavigableMap<K, V>, Cloneable, Serializable {
    private static final VarHandle ADDER;

    /* renamed from: EQ */
    private static final int f737EQ = 1;

    /* renamed from: GT */
    private static final int f738GT = 0;
    private static final VarHandle HEAD;

    /* renamed from: LT */
    private static final int f739LT = 2;
    private static final VarHandle NEXT;
    private static final VarHandle RIGHT;
    private static final VarHandle VAL;
    private static final long serialVersionUID = -8627078645895051609L;
    private transient LongAdder adder;
    final Comparator<? super K> comparator;
    private transient SubMap<K, V> descendingMap;
    private transient EntrySet<K, V> entrySet;
    private transient Index<K, V> head;
    private transient KeySet<K, V> keySet;
    private transient Values<K, V> values;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.addCount(long):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private void addCount(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.addCount(long):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.addCount(long):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.addIndices(java.util.concurrent.ConcurrentSkipListMap$Index, int, java.util.concurrent.ConcurrentSkipListMap$Index, java.util.Comparator):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    static <K, V> boolean addIndices(java.util.concurrent.ConcurrentSkipListMap.Index<K, V> r1, int r2, java.util.concurrent.ConcurrentSkipListMap.Index<K, V> r3, java.util.Comparator<? super K> r4) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.addIndices(java.util.concurrent.ConcurrentSkipListMap$Index, int, java.util.concurrent.ConcurrentSkipListMap$Index, java.util.Comparator):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.addIndices(java.util.concurrent.ConcurrentSkipListMap$Index, int, java.util.concurrent.ConcurrentSkipListMap$Index, java.util.Comparator):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doGet(java.lang.Object):V, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private V doGet(java.lang.Object r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doGet(java.lang.Object):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.doGet(java.lang.Object):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doPut(java.lang.Object, java.lang.Object, boolean):V, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private V doPut(K r1, V r2, boolean r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doPut(java.lang.Object, java.lang.Object, boolean):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.doPut(java.lang.Object, java.lang.Object, boolean):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doRemoveFirstEntry():java.util.AbstractMap$SimpleImmutableEntry<K, V>, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private java.util.AbstractMap.SimpleImmutableEntry<K, V> doRemoveFirstEntry() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doRemoveFirstEntry():java.util.AbstractMap$SimpleImmutableEntry<K, V>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.doRemoveFirstEntry():java.util.AbstractMap$SimpleImmutableEntry");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doRemoveLastEntry():java.util.Map$Entry<K, V>, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private java.util.Map.Entry<K, V> doRemoveLastEntry() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doRemoveLastEntry():java.util.Map$Entry<K, V>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.doRemoveLastEntry():java.util.Map$Entry");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.findPredecessor(java.lang.Object, java.util.Comparator):java.util.concurrent.ConcurrentSkipListMap$Node<K, V>, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private java.util.concurrent.ConcurrentSkipListMap.Node<K, V> findPredecessor(java.lang.Object r1, java.util.Comparator<? super K> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.findPredecessor(java.lang.Object, java.util.Comparator):java.util.concurrent.ConcurrentSkipListMap$Node<K, V>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.findPredecessor(java.lang.Object, java.util.Comparator):java.util.concurrent.ConcurrentSkipListMap$Node");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.tryReduceLevel():void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private void tryReduceLevel() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.tryReduceLevel():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.tryReduceLevel():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.unlinkNode(java.util.concurrent.ConcurrentSkipListMap$Node, java.util.concurrent.ConcurrentSkipListMap$Node):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    static <K, V> void unlinkNode(java.util.concurrent.ConcurrentSkipListMap.Node<K, V> r1, java.util.concurrent.ConcurrentSkipListMap.Node<K, V> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.unlinkNode(java.util.concurrent.ConcurrentSkipListMap$Node, java.util.concurrent.ConcurrentSkipListMap$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.unlinkNode(java.util.concurrent.ConcurrentSkipListMap$Node, java.util.concurrent.ConcurrentSkipListMap$Node):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.clear():void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public void clear() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.clear():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.clear():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.compute(java.lang.Object, java.util.function.BiFunction):V, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public V compute(K r1, java.util.function.BiFunction<? super K, ? super V, ? extends V> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.compute(java.lang.Object, java.util.function.BiFunction):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.compute(java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.computeIfPresent(java.lang.Object, java.util.function.BiFunction):V, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public V computeIfPresent(K r1, java.util.function.BiFunction<? super K, ? super V, ? extends V> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.computeIfPresent(java.lang.Object, java.util.function.BiFunction):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.computeIfPresent(java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doRemove(java.lang.Object, java.lang.Object):V, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final V doRemove(java.lang.Object r1, java.lang.Object r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.doRemove(java.lang.Object, java.lang.Object):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.doRemove(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.findLast():java.util.concurrent.ConcurrentSkipListMap$Node<K, V>, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final java.util.concurrent.ConcurrentSkipListMap.Node<K, V> findLast() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.findLast():java.util.concurrent.ConcurrentSkipListMap$Node<K, V>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.findLast():java.util.concurrent.ConcurrentSkipListMap$Node");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.getAdderCount():long, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final long getAdderCount() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.getAdderCount():long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.getAdderCount():long");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction):V, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public V merge(K r1, V r2, java.util.function.BiFunction<? super V, ? super V, ? extends V> r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.replace(java.lang.Object, java.lang.Object):V, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public V replace(K r1, V r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.replace(java.lang.Object, java.lang.Object):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.replace(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.replace(java.lang.Object, java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public boolean replace(K r1, V r2, V r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.replace(java.lang.Object, java.lang.Object, java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.replace(java.lang.Object, java.lang.Object, java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.replaceAll(java.util.function.BiFunction):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public void replaceAll(java.util.function.BiFunction<? super K, ? super V, ? extends V> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentSkipListMap.replaceAll(java.util.function.BiFunction):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.replaceAll(java.util.function.BiFunction):void");
    }

    static final class Node<K, V> {
        final K key;
        Node<K, V> next;
        V val;

        Node(K k, V v, Node<K, V> node) {
            this.key = k;
            this.val = v;
            this.next = node;
        }
    }

    static final class Index<K, V> {
        final Index<K, V> down;
        final Node<K, V> node;
        Index<K, V> right;

        Index(Node<K, V> node2, Index<K, V> index, Index<K, V> index2) {
            this.node = node2;
            this.down = index;
            this.right = index2;
        }
    }

    static int cpr(Comparator comparator2, Object obj, Object obj2) {
        return comparator2 != null ? comparator2.compare(obj, obj2) : ((Comparable) obj).compareTo(obj2);
    }

    /* access modifiers changed from: package-private */
    public final Node<K, V> baseHead() {
        VarHandle.acquireFence();
        Index<K, V> index = this.head;
        if (index == null) {
            return null;
        }
        return index.node;
    }

    private Node<K, V> findNode(Object obj) {
        obj.getClass();
        Comparator<? super K> comparator2 = this.comparator;
        while (true) {
            Node<K, V> findPredecessor = findPredecessor(obj, comparator2);
            if (findPredecessor == null) {
                return null;
            }
            while (true) {
                Node<K, V> node = findPredecessor.next;
                if (node == null) {
                    return null;
                }
                K k = node.key;
                if (k != null) {
                    if (node.val == null) {
                        unlinkNode(findPredecessor, node);
                    } else {
                        int cpr = cpr(comparator2, obj, k);
                        if (cpr > 0) {
                            findPredecessor = node;
                        } else if (cpr == 0) {
                            return node;
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final Node<K, V> findFirst() {
        Node baseHead = baseHead();
        if (baseHead == null) {
            return null;
        }
        while (true) {
            Node<K, V> node = baseHead.next;
            if (node == null) {
                return null;
            }
            if (node.val != null) {
                return node;
            }
            unlinkNode(baseHead, node);
        }
    }

    /* access modifiers changed from: package-private */
    public final AbstractMap.SimpleImmutableEntry<K, V> findFirstEntry() {
        Node baseHead = baseHead();
        if (baseHead == null) {
            return null;
        }
        while (true) {
            Node<K, V> node = baseHead.next;
            if (node == null) {
                return null;
            }
            V v = node.val;
            if (v != null) {
                return new AbstractMap.SimpleImmutableEntry<>(node.key, v);
            }
            unlinkNode(baseHead, node);
        }
    }

    /* access modifiers changed from: package-private */
    public final AbstractMap.SimpleImmutableEntry<K, V> findLastEntry() {
        Node findLast;
        V v;
        do {
            findLast = findLast();
            if (findLast == null) {
                return null;
            }
            v = findLast.val;
        } while (v == null);
        return new AbstractMap.SimpleImmutableEntry<>(findLast.key, v);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.concurrent.ConcurrentSkipListMap.Node<K, V> findNear(K r6, int r7, java.util.Comparator<? super K> r8) {
        /*
            r5 = this;
            r6.getClass()
        L_0x0003:
            java.util.concurrent.ConcurrentSkipListMap$Node r0 = r5.findPredecessor(r6, r8)
            r1 = 0
            if (r0 != 0) goto L_0x000b
            goto L_0x0043
        L_0x000b:
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r2 = r0.next
            if (r2 != 0) goto L_0x0019
            r5 = r7 & 2
            if (r5 == 0) goto L_0x0043
            K r5 = r0.key
            if (r5 == 0) goto L_0x0043
        L_0x0017:
            r1 = r0
            goto L_0x0043
        L_0x0019:
            K r3 = r2.key
            if (r3 != 0) goto L_0x001e
            goto L_0x0003
        L_0x001e:
            V r4 = r2.val
            if (r4 != 0) goto L_0x0026
            unlinkNode(r0, r2)
            goto L_0x000b
        L_0x0026:
            int r3 = cpr(r8, r6, r3)
            if (r3 != 0) goto L_0x0030
            r4 = r7 & 1
            if (r4 != 0) goto L_0x0036
        L_0x0030:
            if (r3 >= 0) goto L_0x0038
            r4 = r7 & 2
            if (r4 != 0) goto L_0x0038
        L_0x0036:
            r1 = r2
            goto L_0x0043
        L_0x0038:
            if (r3 > 0) goto L_0x0044
            r3 = r7 & 2
            if (r3 == 0) goto L_0x0044
            K r5 = r0.key
            if (r5 == 0) goto L_0x0043
            goto L_0x0017
        L_0x0043:
            return r1
        L_0x0044:
            r0 = r2
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.findNear(java.lang.Object, int, java.util.Comparator):java.util.concurrent.ConcurrentSkipListMap$Node");
    }

    /* access modifiers changed from: package-private */
    public final AbstractMap.SimpleImmutableEntry<K, V> findNearEntry(K k, int i, Comparator<? super K> comparator2) {
        Node<K, V> findNear;
        V v;
        do {
            findNear = findNear(k, i, comparator2);
            if (findNear == null) {
                return null;
            }
            v = findNear.val;
        } while (v == null);
        return new AbstractMap.SimpleImmutableEntry<>(findNear.key, v);
    }

    public ConcurrentSkipListMap() {
        this.comparator = null;
    }

    public ConcurrentSkipListMap(Comparator<? super K> comparator2) {
        this.comparator = comparator2;
    }

    public ConcurrentSkipListMap(Map<? extends K, ? extends V> map) {
        this.comparator = null;
        putAll(map);
    }

    public ConcurrentSkipListMap(SortedMap<K, ? extends V> sortedMap) {
        this.comparator = sortedMap.comparator();
        buildFromSorted(sortedMap);
    }

    public ConcurrentSkipListMap<K, V> clone() {
        try {
            ConcurrentSkipListMap<K, V> concurrentSkipListMap = (ConcurrentSkipListMap) super.clone();
            concurrentSkipListMap.keySet = null;
            concurrentSkipListMap.entrySet = null;
            concurrentSkipListMap.values = null;
            concurrentSkipListMap.descendingMap = null;
            concurrentSkipListMap.adder = null;
            concurrentSkipListMap.buildFromSorted(this);
            return concurrentSkipListMap;
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: java.util.concurrent.ConcurrentSkipListMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.util.concurrent.ConcurrentSkipListMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: java.util.concurrent.ConcurrentSkipListMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v9, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v6, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v8, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void buildFromSorted(java.util.SortedMap<K, ? extends V> r21) {
        /*
            r20 = this;
            r0 = r20
            r21.getClass()
            java.util.Set r1 = r21.entrySet()
            java.util.Iterator r1 = r1.iterator()
            r2 = 64
            java.util.concurrent.ConcurrentSkipListMap$Index[] r3 = new java.util.concurrent.ConcurrentSkipListMap.Index[r2]
            java.util.concurrent.ConcurrentSkipListMap$Node r4 = new java.util.concurrent.ConcurrentSkipListMap$Node
            r5 = 0
            r4.<init>(r5, r5, r5)
            java.util.concurrent.ConcurrentSkipListMap$Index r6 = new java.util.concurrent.ConcurrentSkipListMap$Index
            r6.<init>(r4, r5, r5)
            r7 = 0
            r3[r7] = r6
            r8 = 0
            r10 = r8
        L_0x0022:
            boolean r12 = r1.hasNext()
            if (r12 == 0) goto L_0x0081
            java.lang.Object r12 = r1.next()
            java.util.Map$Entry r12 = (java.util.Map.Entry) r12
            java.lang.Object r13 = r12.getKey()
            java.lang.Object r12 = r12.getValue()
            if (r13 == 0) goto L_0x007f
            if (r12 == 0) goto L_0x007f
            java.util.concurrent.ConcurrentSkipListMap$Node r14 = new java.util.concurrent.ConcurrentSkipListMap$Node
            r14.<init>(r13, r12, r5)
            r4.next = r14
            r12 = 1
            long r10 = r10 + r12
            r15 = 3
            long r15 = r15 & r10
            int r4 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
            if (r4 != 0) goto L_0x007b
            r4 = 2
            long r15 = r10 >>> r4
            r4 = r5
            r17 = r7
        L_0x0051:
            java.util.concurrent.ConcurrentSkipListMap$Index r7 = new java.util.concurrent.ConcurrentSkipListMap$Index
            r7.<init>(r14, r4, r5)
            r4 = r3[r17]
            if (r4 != 0) goto L_0x0065
            java.util.concurrent.ConcurrentSkipListMap$Index r4 = new java.util.concurrent.ConcurrentSkipListMap$Index
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r5 = r6.node
            r4.<init>(r5, r6, r7)
            r3[r17] = r4
            r6 = r4
            goto L_0x0069
        L_0x0065:
            r4.right = r7
            r3[r17] = r7
        L_0x0069:
            int r4 = r17 + 1
            if (r4 >= r2) goto L_0x007b
            r5 = 1
            long r15 = r15 >>> r5
            long r18 = r15 & r12
            int r5 = (r18 > r8 ? 1 : (r18 == r8 ? 0 : -1))
            if (r5 != 0) goto L_0x0076
            goto L_0x007b
        L_0x0076:
            r17 = r4
            r4 = r7
            r5 = 0
            goto L_0x0051
        L_0x007b:
            r4 = r14
            r5 = 0
            r7 = 0
            goto L_0x0022
        L_0x007f:
            r0 = r5
            throw r0
        L_0x0081:
            int r1 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r1 == 0) goto L_0x0090
            java.lang.invoke.VarHandle.releaseFence()
            r0.addCount(r10)
            r0.head = r6
            java.lang.invoke.VarHandle.fullFence()
        L_0x0090:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.buildFromSorted(java.util.SortedMap):void");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Node<K, V> baseHead = baseHead();
        if (baseHead != null) {
            while (true) {
                baseHead = baseHead.next;
                if (baseHead == null) {
                    break;
                }
                V v = baseHead.val;
                if (v != null) {
                    objectOutputStream.writeObject(baseHead.key);
                    objectOutputStream.writeObject(v);
                }
            }
        }
        objectOutputStream.writeObject((Object) null);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v0, resolved type: java.util.concurrent.ConcurrentSkipListMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.util.concurrent.ConcurrentSkipListMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: java.util.concurrent.ConcurrentSkipListMap$Node} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index<K, V>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: java.util.concurrent.ConcurrentSkipListMap$Index} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readObject(java.p026io.ObjectInputStream r22) throws java.p026io.IOException, java.lang.ClassNotFoundException {
        /*
            r21 = this;
            r0 = r21
            r22.defaultReadObject()
            r1 = 64
            java.util.concurrent.ConcurrentSkipListMap$Index[] r2 = new java.util.concurrent.ConcurrentSkipListMap.Index[r1]
            java.util.concurrent.ConcurrentSkipListMap$Node r3 = new java.util.concurrent.ConcurrentSkipListMap$Node
            r4 = 0
            r3.<init>(r4, r4, r4)
            java.util.concurrent.ConcurrentSkipListMap$Index r5 = new java.util.concurrent.ConcurrentSkipListMap$Index
            r5.<init>(r3, r4, r4)
            r6 = 0
            r2[r6] = r5
            java.util.Comparator<? super K> r7 = r0.comparator
            r8 = 0
            r10 = r4
            r11 = r8
        L_0x001d:
            java.lang.Object r13 = r22.readObject()
            if (r13 != 0) goto L_0x0033
            int r1 = (r11 > r8 ? 1 : (r11 == r8 ? 0 : -1))
            if (r1 == 0) goto L_0x0032
            java.lang.invoke.VarHandle.releaseFence()
            r0.addCount(r11)
            r0.head = r5
            java.lang.invoke.VarHandle.fullFence()
        L_0x0032:
            return
        L_0x0033:
            java.lang.Object r14 = r22.readObject()
            r14.getClass()
            if (r10 == 0) goto L_0x004b
            int r10 = cpr(r7, r10, r13)
            if (r10 > 0) goto L_0x0043
            goto L_0x004b
        L_0x0043:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "out of order"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x004b:
            java.util.concurrent.ConcurrentSkipListMap$Node r10 = new java.util.concurrent.ConcurrentSkipListMap$Node
            r10.<init>(r13, r14, r4)
            r3.next = r10
            r14 = 1
            long r11 = r11 + r14
            r16 = 3
            long r16 = r11 & r16
            int r3 = (r16 > r8 ? 1 : (r16 == r8 ? 0 : -1))
            if (r3 != 0) goto L_0x008e
            r3 = 2
            long r16 = r11 >>> r3
            r3 = r4
            r18 = r6
        L_0x0063:
            java.util.concurrent.ConcurrentSkipListMap$Index r6 = new java.util.concurrent.ConcurrentSkipListMap$Index
            r6.<init>(r10, r3, r4)
            r3 = r2[r18]
            if (r3 != 0) goto L_0x0077
            java.util.concurrent.ConcurrentSkipListMap$Index r3 = new java.util.concurrent.ConcurrentSkipListMap$Index
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r4 = r5.node
            r3.<init>(r4, r5, r6)
            r2[r18] = r3
            r5 = r3
            goto L_0x007b
        L_0x0077:
            r3.right = r6
            r2[r18] = r6
        L_0x007b:
            int r3 = r18 + 1
            if (r3 >= r1) goto L_0x008e
            r4 = 1
            long r16 = r16 >>> r4
            long r19 = r16 & r14
            int r4 = (r19 > r8 ? 1 : (r19 == r8 ? 0 : -1))
            if (r4 != 0) goto L_0x0089
            goto L_0x008e
        L_0x0089:
            r18 = r3
            r3 = r6
            r4 = 0
            goto L_0x0063
        L_0x008e:
            r3 = r10
            r10 = r13
            r4 = 0
            r6 = 0
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.readObject(java.io.ObjectInputStream):void");
    }

    public boolean containsKey(Object obj) {
        return doGet(obj) != null;
    }

    public V get(Object obj) {
        return doGet(obj);
    }

    public V getOrDefault(Object obj, V v) {
        V doGet = doGet(obj);
        return doGet == null ? v : doGet;
    }

    public V put(K k, V v) {
        v.getClass();
        return doPut(k, v, false);
    }

    public V remove(Object obj) {
        return doRemove(obj, (Object) null);
    }

    public boolean containsValue(Object obj) {
        obj.getClass();
        Node<K, V> baseHead = baseHead();
        if (baseHead == null) {
            return false;
        }
        while (true) {
            baseHead = baseHead.next;
            if (baseHead == null) {
                return false;
            }
            V v = baseHead.val;
            if (v != null && obj.equals(v)) {
                return true;
            }
        }
    }

    public int size() {
        if (baseHead() == null) {
            return 0;
        }
        long adderCount = getAdderCount();
        if (adderCount >= 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) adderCount;
    }

    public boolean isEmpty() {
        return findFirst() == null;
    }

    public V computeIfAbsent(K k, Function<? super K, ? extends V> function) {
        V apply;
        if (k == null || function == null) {
            throw null;
        }
        V doGet = doGet(k);
        if (doGet != null || (apply = function.apply(k)) == null) {
            return doGet;
        }
        V doPut = doPut(k, apply, true);
        return doPut == null ? apply : doPut;
    }

    public NavigableSet<K> keySet() {
        KeySet<K, V> keySet2 = this.keySet;
        if (keySet2 != null) {
            return keySet2;
        }
        KeySet<K, V> keySet3 = new KeySet<>(this);
        this.keySet = keySet3;
        return keySet3;
    }

    public NavigableSet<K> navigableKeySet() {
        KeySet<K, V> keySet2 = this.keySet;
        if (keySet2 != null) {
            return keySet2;
        }
        KeySet<K, V> keySet3 = new KeySet<>(this);
        this.keySet = keySet3;
        return keySet3;
    }

    public Collection<V> values() {
        Values<K, V> values2 = this.values;
        if (values2 != null) {
            return values2;
        }
        Values<K, V> values3 = new Values<>(this);
        this.values = values3;
        return values3;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        EntrySet<K, V> entrySet2 = this.entrySet;
        if (entrySet2 != null) {
            return entrySet2;
        }
        EntrySet<K, V> entrySet3 = new EntrySet<>(this);
        this.entrySet = entrySet3;
        return entrySet3;
    }

    public ConcurrentNavigableMap<K, V> descendingMap() {
        SubMap<K, V> subMap = this.descendingMap;
        if (subMap != null) {
            return subMap;
        }
        SubMap subMap2 = new SubMap(this, null, false, null, false, true);
        this.descendingMap = subMap2;
        return subMap2;
    }

    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0069 A[Catch:{ ClassCastException | NullPointerException -> 0x00a8 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r8) {
        /*
            r7 = this;
            r0 = 1
            if (r8 != r7) goto L_0x0004
            return r0
        L_0x0004:
            boolean r1 = r8 instanceof java.util.Map
            r2 = 0
            if (r1 != 0) goto L_0x000a
            return r2
        L_0x000a:
            java.util.Map r8 = (java.util.Map) r8
            java.util.Comparator<? super K> r1 = r7.comparator     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.util.Set r3 = r8.entrySet()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            boolean r4 = r8 instanceof java.util.SortedMap     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r4 == 0) goto L_0x0063
            r4 = r8
            java.util.SortedMap r4 = (java.util.SortedMap) r4     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.util.Comparator r4 = r4.comparator()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r4 != r1) goto L_0x0063
            java.util.concurrent.ConcurrentSkipListMap$Node r7 = r7.baseHead()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r7 == 0) goto L_0x005d
        L_0x0029:
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r7 = r7.next     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r7 == 0) goto L_0x005d
            V r8 = r7.val     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r8 == 0) goto L_0x0029
            K r4 = r7.key     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r4 == 0) goto L_0x0029
            boolean r5 = r3.hasNext()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r5 != 0) goto L_0x003c
            return r2
        L_0x003c:
            java.lang.Object r5 = r3.next()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.lang.Object r6 = r5.getKey()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.lang.Object r5 = r5.getValue()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r6 == 0) goto L_0x005c
            if (r5 != 0) goto L_0x004f
            goto L_0x005c
        L_0x004f:
            int r4 = cpr(r1, r4, r6)     // Catch:{ ClassCastException -> 0x005c }
            if (r4 == 0) goto L_0x0056
            return r2
        L_0x0056:
            boolean r8 = r5.equals(r8)     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r8 != 0) goto L_0x0029
        L_0x005c:
            return r2
        L_0x005d:
            boolean r7 = r3.hasNext()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            r7 = r7 ^ r0
            return r7
        L_0x0063:
            boolean r1 = r3.hasNext()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r1 == 0) goto L_0x0088
            java.lang.Object r1 = r3.next()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.lang.Object r4 = r1.getKey()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            java.lang.Object r1 = r1.getValue()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r4 == 0) goto L_0x0087
            if (r1 == 0) goto L_0x0087
            java.lang.Object r4 = r7.get(r4)     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r4 == 0) goto L_0x0087
            boolean r1 = r4.equals(r1)     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r1 != 0) goto L_0x0063
        L_0x0087:
            return r2
        L_0x0088:
            java.util.concurrent.ConcurrentSkipListMap$Node r7 = r7.baseHead()     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r7 == 0) goto L_0x00a7
        L_0x008e:
            java.util.concurrent.ConcurrentSkipListMap$Node<K, V> r7 = r7.next     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r7 == 0) goto L_0x00a7
            V r1 = r7.val     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r1 == 0) goto L_0x008e
            K r3 = r7.key     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r3 == 0) goto L_0x008e
            java.lang.Object r3 = r8.get(r3)     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r3 == 0) goto L_0x00a6
            boolean r1 = r3.equals(r1)     // Catch:{ ClassCastException | NullPointerException -> 0x00a8 }
            if (r1 != 0) goto L_0x008e
        L_0x00a6:
            return r2
        L_0x00a7:
            return r0
        L_0x00a8:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.equals(java.lang.Object):boolean");
    }

    public V putIfAbsent(K k, V v) {
        v.getClass();
        return doPut(k, v, true);
    }

    public boolean remove(Object obj, Object obj2) {
        obj.getClass();
        return (obj2 == null || doRemove(obj, obj2) == null) ? false : true;
    }

    public Comparator<? super K> comparator() {
        return this.comparator;
    }

    public K firstKey() {
        Node findFirst = findFirst();
        if (findFirst != null) {
            return findFirst.key;
        }
        throw new NoSuchElementException();
    }

    public K lastKey() {
        Node findLast = findLast();
        if (findLast != null) {
            return findLast.key;
        }
        throw new NoSuchElementException();
    }

    public ConcurrentNavigableMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
        if (k != null && k2 != null) {
            return new SubMap(this, k, z, k2, z2, false);
        }
        throw null;
    }

    public ConcurrentNavigableMap<K, V> headMap(K k, boolean z) {
        k.getClass();
        return new SubMap(this, null, false, k, z, false);
    }

    public ConcurrentNavigableMap<K, V> tailMap(K k, boolean z) {
        k.getClass();
        return new SubMap(this, k, z, null, false, false);
    }

    public ConcurrentNavigableMap<K, V> subMap(K k, K k2) {
        return subMap((Object) k, true, (Object) k2, false);
    }

    public ConcurrentNavigableMap<K, V> headMap(K k) {
        return headMap((Object) k, false);
    }

    public ConcurrentNavigableMap<K, V> tailMap(K k) {
        return tailMap((Object) k, true);
    }

    public Map.Entry<K, V> lowerEntry(K k) {
        return findNearEntry(k, 2, this.comparator);
    }

    public K lowerKey(K k) {
        Node<K, V> findNear = findNear(k, 2, this.comparator);
        if (findNear == null) {
            return null;
        }
        return findNear.key;
    }

    public Map.Entry<K, V> floorEntry(K k) {
        return findNearEntry(k, 3, this.comparator);
    }

    public K floorKey(K k) {
        Node<K, V> findNear = findNear(k, 3, this.comparator);
        if (findNear == null) {
            return null;
        }
        return findNear.key;
    }

    public Map.Entry<K, V> ceilingEntry(K k) {
        return findNearEntry(k, 1, this.comparator);
    }

    public K ceilingKey(K k) {
        Node<K, V> findNear = findNear(k, 1, this.comparator);
        if (findNear == null) {
            return null;
        }
        return findNear.key;
    }

    public Map.Entry<K, V> higherEntry(K k) {
        return findNearEntry(k, 0, this.comparator);
    }

    public K higherKey(K k) {
        Node<K, V> findNear = findNear(k, 0, this.comparator);
        if (findNear == null) {
            return null;
        }
        return findNear.key;
    }

    public Map.Entry<K, V> firstEntry() {
        return findFirstEntry();
    }

    public Map.Entry<K, V> lastEntry() {
        return findLastEntry();
    }

    public Map.Entry<K, V> pollFirstEntry() {
        return doRemoveFirstEntry();
    }

    public Map.Entry<K, V> pollLastEntry() {
        return doRemoveLastEntry();
    }

    abstract class Iter<T> implements Iterator<T> {
        Node<K, V> lastReturned;
        Node<K, V> next;
        V nextValue;

        Iter() {
            advance(ConcurrentSkipListMap.this.baseHead());
        }

        public final boolean hasNext() {
            return this.next != null;
        }

        /* access modifiers changed from: package-private */
        public final void advance(Node<K, V> node) {
            this.lastReturned = node;
            V v = null;
            if (node != null) {
                do {
                    node = node.next;
                    if (node == null) {
                        break;
                    }
                    v = node.val;
                } while (v != null);
            } else {
                node = null;
            }
            this.nextValue = v;
            this.next = node;
        }

        public final void remove() {
            K k;
            Node<K, V> node = this.lastReturned;
            if (node == null || (k = node.key) == null) {
                throw new IllegalStateException();
            }
            ConcurrentSkipListMap.this.remove(k);
            this.lastReturned = null;
        }
    }

    final class ValueIterator extends ConcurrentSkipListMap<K, V>.Iter<V> {
        ValueIterator() {
            super();
        }

        public V next() {
            V v = this.nextValue;
            if (v != null) {
                advance(this.next);
                return v;
            }
            throw new NoSuchElementException();
        }
    }

    final class KeyIterator extends ConcurrentSkipListMap<K, V>.Iter<K> {
        KeyIterator() {
            super();
        }

        public K next() {
            Node node = this.next;
            if (node != null) {
                K k = node.key;
                advance(node);
                return k;
            }
            throw new NoSuchElementException();
        }
    }

    final class EntryIterator extends ConcurrentSkipListMap<K, V>.Iter<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        public Map.Entry<K, V> next() {
            Node node = this.next;
            if (node != null) {
                K k = node.key;
                Object obj = this.nextValue;
                advance(node);
                return new AbstractMap.SimpleImmutableEntry(k, obj);
            }
            throw new NoSuchElementException();
        }
    }

    static final <E> List<E> toList(Collection<E> collection) {
        ArrayList arrayList = new ArrayList();
        for (E add : collection) {
            arrayList.add(add);
        }
        return arrayList;
    }

    static final class KeySet<K, V> extends AbstractSet<K> implements NavigableSet<K> {

        /* renamed from: m */
        final ConcurrentNavigableMap<K, V> f741m;

        KeySet(ConcurrentNavigableMap<K, V> concurrentNavigableMap) {
            this.f741m = concurrentNavigableMap;
        }

        public int size() {
            return this.f741m.size();
        }

        public boolean isEmpty() {
            return this.f741m.isEmpty();
        }

        public boolean contains(Object obj) {
            return this.f741m.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return this.f741m.remove(obj) != null;
        }

        public void clear() {
            this.f741m.clear();
        }

        public K lower(K k) {
            return this.f741m.lowerKey(k);
        }

        public K floor(K k) {
            return this.f741m.floorKey(k);
        }

        public K ceiling(K k) {
            return this.f741m.ceilingKey(k);
        }

        public K higher(K k) {
            return this.f741m.higherKey(k);
        }

        public Comparator<? super K> comparator() {
            return this.f741m.comparator();
        }

        public K first() {
            return this.f741m.firstKey();
        }

        public K last() {
            return this.f741m.lastKey();
        }

        public K pollFirst() {
            Map.Entry<K, V> pollFirstEntry = this.f741m.pollFirstEntry();
            if (pollFirstEntry == null) {
                return null;
            }
            return pollFirstEntry.getKey();
        }

        public K pollLast() {
            Map.Entry<K, V> pollLastEntry = this.f741m.pollLastEntry();
            if (pollLastEntry == null) {
                return null;
            }
            return pollLastEntry.getKey();
        }

        public Iterator<K> iterator() {
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f741m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                ConcurrentSkipListMap concurrentSkipListMap = (ConcurrentSkipListMap) concurrentNavigableMap;
                Objects.requireNonNull(concurrentSkipListMap);
                return new KeyIterator();
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            return new SubMap.SubMapKeyIterator();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Set)) {
                return false;
            }
            Collection collection = (Collection) obj;
            try {
                if (!containsAll(collection) || !collection.containsAll(this)) {
                    return false;
                }
                return true;
            } catch (ClassCastException | NullPointerException unused) {
                return false;
            }
        }

        public Object[] toArray() {
            return ConcurrentSkipListMap.toList(this).toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return ConcurrentSkipListMap.toList(this).toArray(tArr);
        }

        public Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        public NavigableSet<K> subSet(K k, boolean z, K k2, boolean z2) {
            return new KeySet(this.f741m.subMap((Object) k, z, (Object) k2, z2));
        }

        public NavigableSet<K> headSet(K k, boolean z) {
            return new KeySet(this.f741m.headMap((Object) k, z));
        }

        public NavigableSet<K> tailSet(K k, boolean z) {
            return new KeySet(this.f741m.tailMap((Object) k, z));
        }

        public NavigableSet<K> subSet(K k, K k2) {
            return subSet(k, true, k2, false);
        }

        public NavigableSet<K> headSet(K k) {
            return headSet(k, false);
        }

        public NavigableSet<K> tailSet(K k) {
            return tailSet(k, true);
        }

        public NavigableSet<K> descendingSet() {
            return new KeySet(this.f741m.descendingMap());
        }

        public Spliterator<K> spliterator() {
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f741m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) concurrentNavigableMap).keySpliterator();
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            return new SubMap.SubMapKeyIterator();
        }
    }

    static final class Values<K, V> extends AbstractCollection<V> {

        /* renamed from: m */
        final ConcurrentNavigableMap<K, V> f745m;

        Values(ConcurrentNavigableMap<K, V> concurrentNavigableMap) {
            this.f745m = concurrentNavigableMap;
        }

        public Iterator<V> iterator() {
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f745m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                ConcurrentSkipListMap concurrentSkipListMap = (ConcurrentSkipListMap) concurrentNavigableMap;
                Objects.requireNonNull(concurrentSkipListMap);
                return new ValueIterator();
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            return new SubMap.SubMapValueIterator();
        }

        public int size() {
            return this.f745m.size();
        }

        public boolean isEmpty() {
            return this.f745m.isEmpty();
        }

        public boolean contains(Object obj) {
            return this.f745m.containsValue(obj);
        }

        public void clear() {
            this.f745m.clear();
        }

        public Object[] toArray() {
            return ConcurrentSkipListMap.toList(this).toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return ConcurrentSkipListMap.toList(this).toArray(tArr);
        }

        public Spliterator<V> spliterator() {
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f745m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) concurrentNavigableMap).valueSpliterator();
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            return new SubMap.SubMapValueIterator();
        }

        public boolean removeIf(Predicate<? super V> predicate) {
            predicate.getClass();
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f745m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) concurrentNavigableMap).removeValueIf(predicate);
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            SubMap.SubMapEntryIterator subMapEntryIterator = new SubMap.SubMapEntryIterator();
            boolean z = false;
            while (subMapEntryIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) subMapEntryIterator.next();
                Object value = entry.getValue();
                if (predicate.test(value) && this.f745m.remove(entry.getKey(), value)) {
                    z = true;
                }
            }
            return z;
        }
    }

    static final class EntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> {

        /* renamed from: m */
        final ConcurrentNavigableMap<K, V> f740m;

        EntrySet(ConcurrentNavigableMap<K, V> concurrentNavigableMap) {
            this.f740m = concurrentNavigableMap;
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f740m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                ConcurrentSkipListMap concurrentSkipListMap = (ConcurrentSkipListMap) concurrentNavigableMap;
                Objects.requireNonNull(concurrentSkipListMap);
                return new EntryIterator();
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            return new SubMap.SubMapEntryIterator();
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            V v = this.f740m.get(entry.getKey());
            if (v == null || !v.equals(entry.getValue())) {
                return false;
            }
            return true;
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return this.f740m.remove(entry.getKey(), entry.getValue());
        }

        public boolean isEmpty() {
            return this.f740m.isEmpty();
        }

        public int size() {
            return this.f740m.size();
        }

        public void clear() {
            this.f740m.clear();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Set)) {
                return false;
            }
            Collection collection = (Collection) obj;
            try {
                if (!containsAll(collection) || !collection.containsAll(this)) {
                    return false;
                }
                return true;
            } catch (ClassCastException | NullPointerException unused) {
                return false;
            }
        }

        public Object[] toArray() {
            return ConcurrentSkipListMap.toList(this).toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return ConcurrentSkipListMap.toList(this).toArray(tArr);
        }

        public Spliterator<Map.Entry<K, V>> spliterator() {
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f740m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) concurrentNavigableMap).entrySpliterator();
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            return new SubMap.SubMapEntryIterator();
        }

        public boolean removeIf(Predicate<? super Map.Entry<K, V>> predicate) {
            predicate.getClass();
            ConcurrentNavigableMap<K, V> concurrentNavigableMap = this.f740m;
            if (concurrentNavigableMap instanceof ConcurrentSkipListMap) {
                return ((ConcurrentSkipListMap) concurrentNavigableMap).removeEntryIf(predicate);
            }
            SubMap subMap = (SubMap) concurrentNavigableMap;
            Objects.requireNonNull(subMap);
            SubMap.SubMapEntryIterator subMapEntryIterator = new SubMap.SubMapEntryIterator();
            boolean z = false;
            while (subMapEntryIterator.hasNext()) {
                Map.Entry entry = (Map.Entry) subMapEntryIterator.next();
                if (predicate.test(entry) && this.f740m.remove(entry.getKey(), entry.getValue())) {
                    z = true;
                }
            }
            return z;
        }
    }

    static final class SubMap<K, V> extends AbstractMap<K, V> implements ConcurrentNavigableMap<K, V>, Serializable {
        private static final long serialVersionUID = -7647078645895051609L;
        private transient EntrySet<K, V> entrySetView;

        /* renamed from: hi */
        private final K f742hi;
        private final boolean hiInclusive;
        final boolean isDescending;
        private transient KeySet<K, V> keySetView;

        /* renamed from: lo */
        private final K f743lo;
        private final boolean loInclusive;

        /* renamed from: m */
        final ConcurrentSkipListMap<K, V> f744m;
        private transient Values<K, V> valuesView;

        SubMap(ConcurrentSkipListMap<K, V> concurrentSkipListMap, K k, boolean z, K k2, boolean z2, boolean z3) {
            Comparator<? super K> comparator = concurrentSkipListMap.comparator;
            if (k == null || k2 == null || ConcurrentSkipListMap.cpr(comparator, k, k2) <= 0) {
                this.f744m = concurrentSkipListMap;
                this.f743lo = k;
                this.f742hi = k2;
                this.loInclusive = z;
                this.hiInclusive = z2;
                this.isDescending = z3;
                return;
            }
            throw new IllegalArgumentException("inconsistent range");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r2 = java.util.concurrent.ConcurrentSkipListMap.cpr(r3, r2, r0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tooLow(java.lang.Object r2, java.util.Comparator<? super K> r3) {
            /*
                r1 = this;
                K r0 = r1.f743lo
                if (r0 == 0) goto L_0x0012
                int r2 = java.util.concurrent.ConcurrentSkipListMap.cpr(r3, r2, r0)
                if (r2 < 0) goto L_0x0010
                if (r2 != 0) goto L_0x0012
                boolean r1 = r1.loInclusive
                if (r1 != 0) goto L_0x0012
            L_0x0010:
                r1 = 1
                goto L_0x0013
            L_0x0012:
                r1 = 0
            L_0x0013:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.SubMap.tooLow(java.lang.Object, java.util.Comparator):boolean");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r2 = java.util.concurrent.ConcurrentSkipListMap.cpr(r3, r2, r0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean tooHigh(java.lang.Object r2, java.util.Comparator<? super K> r3) {
            /*
                r1 = this;
                K r0 = r1.f742hi
                if (r0 == 0) goto L_0x0012
                int r2 = java.util.concurrent.ConcurrentSkipListMap.cpr(r3, r2, r0)
                if (r2 > 0) goto L_0x0010
                if (r2 != 0) goto L_0x0012
                boolean r1 = r1.hiInclusive
                if (r1 != 0) goto L_0x0012
            L_0x0010:
                r1 = 1
                goto L_0x0013
            L_0x0012:
                r1 = 0
            L_0x0013:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.SubMap.tooHigh(java.lang.Object, java.util.Comparator):boolean");
        }

        /* access modifiers changed from: package-private */
        public boolean inBounds(Object obj, Comparator<? super K> comparator) {
            return !tooLow(obj, comparator) && !tooHigh(obj, comparator);
        }

        /* access modifiers changed from: package-private */
        public void checkKeyBounds(K k, Comparator<? super K> comparator) {
            k.getClass();
            if (!inBounds(k, comparator)) {
                throw new IllegalArgumentException("key out of range");
            }
        }

        /* access modifiers changed from: package-private */
        public boolean isBeforeEnd(Node<K, V> node, Comparator<? super K> comparator) {
            K k;
            if (node == null) {
                return false;
            }
            if (this.f742hi == null || (k = node.key) == null) {
                return true;
            }
            int cpr = ConcurrentSkipListMap.cpr(comparator, k, this.f742hi);
            if (cpr < 0 || (cpr == 0 && this.hiInclusive)) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public Node<K, V> loNode(Comparator<? super K> comparator) {
            K k = this.f743lo;
            if (k == null) {
                return this.f744m.findFirst();
            }
            if (this.loInclusive) {
                return this.f744m.findNear(k, 1, comparator);
            }
            return this.f744m.findNear(k, 0, comparator);
        }

        /* access modifiers changed from: package-private */
        public Node<K, V> hiNode(Comparator<? super K> comparator) {
            K k = this.f742hi;
            if (k == null) {
                return this.f744m.findLast();
            }
            if (this.hiInclusive) {
                return this.f744m.findNear(k, 3, comparator);
            }
            return this.f744m.findNear(k, 2, comparator);
        }

        /* access modifiers changed from: package-private */
        public K lowestKey() {
            Comparator<? super K> comparator = this.f744m.comparator;
            Node<K, V> loNode = loNode(comparator);
            if (isBeforeEnd(loNode, comparator)) {
                return loNode.key;
            }
            throw new NoSuchElementException();
        }

        /* access modifiers changed from: package-private */
        public K highestKey() {
            Comparator<? super K> comparator = this.f744m.comparator;
            Node<K, V> hiNode = hiNode(comparator);
            if (hiNode != null) {
                K k = hiNode.key;
                if (inBounds(k, comparator)) {
                    return k;
                }
            }
            throw new NoSuchElementException();
        }

        /* access modifiers changed from: package-private */
        public Map.Entry<K, V> lowestEntry() {
            Node<K, V> loNode;
            V v;
            Comparator<? super K> comparator = this.f744m.comparator;
            do {
                loNode = loNode(comparator);
                if (loNode == null || !isBeforeEnd(loNode, comparator)) {
                    return null;
                }
                v = loNode.val;
            } while (v == null);
            return new AbstractMap.SimpleImmutableEntry(loNode.key, v);
        }

        /* access modifiers changed from: package-private */
        public Map.Entry<K, V> highestEntry() {
            Node<K, V> hiNode;
            V v;
            Comparator<? super K> comparator = this.f744m.comparator;
            do {
                hiNode = hiNode(comparator);
                if (hiNode == null || !inBounds(hiNode.key, comparator)) {
                    return null;
                }
                v = hiNode.val;
            } while (v == null);
            return new AbstractMap.SimpleImmutableEntry(hiNode.key, v);
        }

        /* access modifiers changed from: package-private */
        public Map.Entry<K, V> removeLowest() {
            K k;
            V doRemove;
            Comparator<? super K> comparator = this.f744m.comparator;
            do {
                Node<K, V> loNode = loNode(comparator);
                if (loNode == null) {
                    return null;
                }
                k = loNode.key;
                if (!inBounds(k, comparator)) {
                    return null;
                }
                doRemove = this.f744m.doRemove(k, (Object) null);
            } while (doRemove == null);
            return new AbstractMap.SimpleImmutableEntry(k, doRemove);
        }

        /* access modifiers changed from: package-private */
        public Map.Entry<K, V> removeHighest() {
            K k;
            V doRemove;
            Comparator<? super K> comparator = this.f744m.comparator;
            do {
                Node<K, V> hiNode = hiNode(comparator);
                if (hiNode == null) {
                    return null;
                }
                k = hiNode.key;
                if (!inBounds(k, comparator)) {
                    return null;
                }
                doRemove = this.f744m.doRemove(k, (Object) null);
            } while (doRemove == null);
            return new AbstractMap.SimpleImmutableEntry(k, doRemove);
        }

        /* access modifiers changed from: package-private */
        public Map.Entry<K, V> getNearEntry(K k, int i) {
            Comparator<? super K> comparator = this.f744m.comparator;
            if (this.isDescending) {
                i = (i & 2) == 0 ? i | 2 : i & -3;
            }
            if (tooLow(k, comparator)) {
                if ((i & 2) != 0) {
                    return null;
                }
                return lowestEntry();
            } else if (!tooHigh(k, comparator)) {
                AbstractMap.SimpleImmutableEntry<K, V> findNearEntry = this.f744m.findNearEntry(k, i, comparator);
                if (findNearEntry == null || !inBounds(findNearEntry.getKey(), comparator)) {
                    return null;
                }
                return findNearEntry;
            } else if ((i & 2) != 0) {
                return highestEntry();
            } else {
                return null;
            }
        }

        /* access modifiers changed from: package-private */
        public K getNearKey(K k, int i) {
            Node<K, V> findNear;
            Node<K, V> hiNode;
            Comparator<? super K> comparator = this.f744m.comparator;
            if (this.isDescending) {
                i = (i & 2) == 0 ? i | 2 : i & -3;
            }
            if (tooLow(k, comparator)) {
                if ((i & 2) == 0) {
                    Node<K, V> loNode = loNode(comparator);
                    if (isBeforeEnd(loNode, comparator)) {
                        return loNode.key;
                    }
                }
                return null;
            } else if (tooHigh(k, comparator)) {
                if (!((i & 2) == 0 || (hiNode = hiNode(comparator)) == null)) {
                    K k2 = hiNode.key;
                    if (inBounds(k2, comparator)) {
                        return k2;
                    }
                }
                return null;
            } else {
                do {
                    findNear = this.f744m.findNear(k, i, comparator);
                    if (findNear == null || !inBounds(findNear.key, comparator)) {
                        return null;
                    }
                } while (findNear.val == null);
                return findNear.key;
            }
        }

        public boolean containsKey(Object obj) {
            obj.getClass();
            return inBounds(obj, this.f744m.comparator) && this.f744m.containsKey(obj);
        }

        public V get(Object obj) {
            obj.getClass();
            if (!inBounds(obj, this.f744m.comparator)) {
                return null;
            }
            return this.f744m.get(obj);
        }

        public V put(K k, V v) {
            checkKeyBounds(k, this.f744m.comparator);
            return this.f744m.put(k, v);
        }

        public V remove(Object obj) {
            if (!inBounds(obj, this.f744m.comparator)) {
                return null;
            }
            return this.f744m.remove(obj);
        }

        public int size() {
            Comparator<? super K> comparator = this.f744m.comparator;
            long j = 0;
            for (Node<K, V> loNode = loNode(comparator); isBeforeEnd(loNode, comparator); loNode = loNode.next) {
                if (loNode.val != null) {
                    j++;
                }
            }
            if (j >= 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) j;
        }

        public boolean isEmpty() {
            Comparator<? super K> comparator = this.f744m.comparator;
            return !isBeforeEnd(loNode(comparator), comparator);
        }

        public boolean containsValue(Object obj) {
            obj.getClass();
            Comparator<? super K> comparator = this.f744m.comparator;
            for (Node<K, V> loNode = loNode(comparator); isBeforeEnd(loNode, comparator); loNode = loNode.next) {
                V v = loNode.val;
                if (v != null && obj.equals(v)) {
                    return true;
                }
            }
            return false;
        }

        public void clear() {
            Comparator<? super K> comparator = this.f744m.comparator;
            for (Node<K, V> loNode = loNode(comparator); isBeforeEnd(loNode, comparator); loNode = loNode.next) {
                if (loNode.val != null) {
                    this.f744m.remove(loNode.key);
                }
            }
        }

        public V putIfAbsent(K k, V v) {
            checkKeyBounds(k, this.f744m.comparator);
            return this.f744m.putIfAbsent(k, v);
        }

        public boolean remove(Object obj, Object obj2) {
            return inBounds(obj, this.f744m.comparator) && this.f744m.remove(obj, obj2);
        }

        public boolean replace(K k, V v, V v2) {
            checkKeyBounds(k, this.f744m.comparator);
            return this.f744m.replace(k, v, v2);
        }

        public V replace(K k, V v) {
            checkKeyBounds(k, this.f744m.comparator);
            return this.f744m.replace(k, v);
        }

        public Comparator<? super K> comparator() {
            Comparator<? super K> comparator = this.f744m.comparator();
            return this.isDescending ? Collections.reverseOrder(comparator) : comparator;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0042  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.concurrent.ConcurrentSkipListMap.SubMap<K, V> newSubMap(K r16, boolean r17, K r18, boolean r19) {
            /*
                r15 = this;
                r0 = r15
                java.util.concurrent.ConcurrentSkipListMap<K, V> r1 = r0.f744m
                java.util.Comparator<? super K> r1 = r1.comparator
                boolean r2 = r0.isDescending
                if (r2 == 0) goto L_0x0012
                r3 = r16
                r5 = r17
                r2 = r18
                r4 = r19
                goto L_0x001a
            L_0x0012:
                r2 = r16
                r4 = r17
                r3 = r18
                r5 = r19
            L_0x001a:
                K r6 = r0.f743lo
                java.lang.String r7 = "key out of range"
                if (r6 == 0) goto L_0x003c
                if (r2 != 0) goto L_0x0027
                boolean r4 = r0.loInclusive
                r11 = r4
                r10 = r6
                goto L_0x003e
            L_0x0027:
                int r6 = java.util.concurrent.ConcurrentSkipListMap.cpr(r1, r2, r6)
                if (r6 < 0) goto L_0x0036
                if (r6 != 0) goto L_0x003c
                boolean r6 = r0.loInclusive
                if (r6 != 0) goto L_0x003c
                if (r4 != 0) goto L_0x0036
                goto L_0x003c
            L_0x0036:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                r0.<init>((java.lang.String) r7)
                throw r0
            L_0x003c:
                r10 = r2
                r11 = r4
            L_0x003e:
                K r2 = r0.f742hi
                if (r2 == 0) goto L_0x005d
                if (r3 != 0) goto L_0x0048
                boolean r5 = r0.hiInclusive
                r12 = r2
                goto L_0x005e
            L_0x0048:
                int r1 = java.util.concurrent.ConcurrentSkipListMap.cpr(r1, r3, r2)
                if (r1 > 0) goto L_0x0057
                if (r1 != 0) goto L_0x005d
                boolean r1 = r0.hiInclusive
                if (r1 != 0) goto L_0x005d
                if (r5 != 0) goto L_0x0057
                goto L_0x005d
            L_0x0057:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                r0.<init>((java.lang.String) r7)
                throw r0
            L_0x005d:
                r12 = r3
            L_0x005e:
                r13 = r5
                java.util.concurrent.ConcurrentSkipListMap$SubMap r1 = new java.util.concurrent.ConcurrentSkipListMap$SubMap
                java.util.concurrent.ConcurrentSkipListMap<K, V> r9 = r0.f744m
                boolean r14 = r0.isDescending
                r8 = r1
                r8.<init>(r9, r10, r11, r12, r13, r14)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentSkipListMap.SubMap.newSubMap(java.lang.Object, boolean, java.lang.Object, boolean):java.util.concurrent.ConcurrentSkipListMap$SubMap");
        }

        public SubMap<K, V> subMap(K k, boolean z, K k2, boolean z2) {
            if (k != null && k2 != null) {
                return newSubMap(k, z, k2, z2);
            }
            throw null;
        }

        public SubMap<K, V> headMap(K k, boolean z) {
            k.getClass();
            return newSubMap((Object) null, false, k, z);
        }

        public SubMap<K, V> tailMap(K k, boolean z) {
            k.getClass();
            return newSubMap(k, z, (K) null, false);
        }

        public SubMap<K, V> subMap(K k, K k2) {
            return subMap((Object) k, true, (Object) k2, false);
        }

        public SubMap<K, V> headMap(K k) {
            return headMap((Object) k, false);
        }

        public SubMap<K, V> tailMap(K k) {
            return tailMap((Object) k, true);
        }

        public SubMap<K, V> descendingMap() {
            return new SubMap(this.f744m, this.f743lo, this.loInclusive, this.f742hi, this.hiInclusive, !this.isDescending);
        }

        public Map.Entry<K, V> ceilingEntry(K k) {
            return getNearEntry(k, 1);
        }

        public K ceilingKey(K k) {
            return getNearKey(k, 1);
        }

        public Map.Entry<K, V> lowerEntry(K k) {
            return getNearEntry(k, 2);
        }

        public K lowerKey(K k) {
            return getNearKey(k, 2);
        }

        public Map.Entry<K, V> floorEntry(K k) {
            return getNearEntry(k, 3);
        }

        public K floorKey(K k) {
            return getNearKey(k, 3);
        }

        public Map.Entry<K, V> higherEntry(K k) {
            return getNearEntry(k, 0);
        }

        public K higherKey(K k) {
            return getNearKey(k, 0);
        }

        public K firstKey() {
            return this.isDescending ? highestKey() : lowestKey();
        }

        public K lastKey() {
            return this.isDescending ? lowestKey() : highestKey();
        }

        public Map.Entry<K, V> firstEntry() {
            return this.isDescending ? highestEntry() : lowestEntry();
        }

        public Map.Entry<K, V> lastEntry() {
            return this.isDescending ? lowestEntry() : highestEntry();
        }

        public Map.Entry<K, V> pollFirstEntry() {
            return this.isDescending ? removeHighest() : removeLowest();
        }

        public Map.Entry<K, V> pollLastEntry() {
            return this.isDescending ? removeLowest() : removeHighest();
        }

        public NavigableSet<K> keySet() {
            KeySet<K, V> keySet = this.keySetView;
            if (keySet != null) {
                return keySet;
            }
            KeySet<K, V> keySet2 = new KeySet<>(this);
            this.keySetView = keySet2;
            return keySet2;
        }

        public NavigableSet<K> navigableKeySet() {
            KeySet<K, V> keySet = this.keySetView;
            if (keySet != null) {
                return keySet;
            }
            KeySet<K, V> keySet2 = new KeySet<>(this);
            this.keySetView = keySet2;
            return keySet2;
        }

        public Collection<V> values() {
            Values<K, V> values = this.valuesView;
            if (values != null) {
                return values;
            }
            Values<K, V> values2 = new Values<>(this);
            this.valuesView = values2;
            return values2;
        }

        public Set<Map.Entry<K, V>> entrySet() {
            EntrySet<K, V> entrySet = this.entrySetView;
            if (entrySet != null) {
                return entrySet;
            }
            EntrySet<K, V> entrySet2 = new EntrySet<>(this);
            this.entrySetView = entrySet2;
            return entrySet2;
        }

        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        abstract class SubMapIter<T> implements Iterator<T>, Spliterator<T> {
            Node<K, V> lastReturned;
            Node<K, V> next;
            V nextValue;

            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            public Spliterator<T> trySplit() {
                return null;
            }

            SubMapIter() {
                V v;
                VarHandle.acquireFence();
                Comparator<? super K> comparator = SubMap.this.f744m.comparator;
                do {
                    Node<K, V> hiNode = SubMap.this.isDescending ? SubMap.this.hiNode(comparator) : SubMap.this.loNode(comparator);
                    this.next = hiNode;
                    if (hiNode != null) {
                        v = hiNode.val;
                    } else {
                        return;
                    }
                } while (v == null);
                if (!SubMap.this.inBounds(this.next.key, comparator)) {
                    this.next = null;
                } else {
                    this.nextValue = v;
                }
            }

            public final boolean hasNext() {
                return this.next != null;
            }

            /* access modifiers changed from: package-private */
            public final void advance() {
                Node<K, V> node = this.next;
                if (node != null) {
                    this.lastReturned = node;
                    if (SubMap.this.isDescending) {
                        descend();
                    } else {
                        ascend();
                    }
                } else {
                    throw new NoSuchElementException();
                }
            }

            private void ascend() {
                V v;
                Comparator<? super K> comparator = SubMap.this.f744m.comparator;
                do {
                    Node<K, V> node = this.next.next;
                    this.next = node;
                    if (node != null) {
                        v = node.val;
                    } else {
                        return;
                    }
                } while (v == null);
                if (SubMap.this.tooHigh(this.next.key, comparator)) {
                    this.next = null;
                } else {
                    this.nextValue = v;
                }
            }

            private void descend() {
                V v;
                Comparator<? super K> comparator = SubMap.this.f744m.comparator;
                do {
                    Node<K, V> findNear = SubMap.this.f744m.findNear(this.lastReturned.key, 2, comparator);
                    this.next = findNear;
                    if (findNear != null) {
                        v = findNear.val;
                    } else {
                        return;
                    }
                } while (v == null);
                if (SubMap.this.tooLow(this.next.key, comparator)) {
                    this.next = null;
                } else {
                    this.nextValue = v;
                }
            }

            public void remove() {
                Node<K, V> node = this.lastReturned;
                if (node != null) {
                    SubMap.this.f744m.remove(node.key);
                    this.lastReturned = null;
                    return;
                }
                throw new IllegalStateException();
            }

            public boolean tryAdvance(Consumer<? super T> consumer) {
                if (!hasNext()) {
                    return false;
                }
                consumer.accept(next());
                return true;
            }

            public void forEachRemaining(Consumer<? super T> consumer) {
                while (hasNext()) {
                    consumer.accept(next());
                }
            }
        }

        final class SubMapValueIterator extends SubMap<K, V>.SubMapIter<V> {
            public int characteristics() {
                return 0;
            }

            SubMapValueIterator() {
                super();
            }

            public V next() {
                V v = this.nextValue;
                advance();
                return v;
            }
        }

        final class SubMapKeyIterator extends SubMap<K, V>.SubMapIter<K> {
            public int characteristics() {
                return 21;
            }

            SubMapKeyIterator() {
                super();
            }

            public K next() {
                Node node = this.next;
                advance();
                return node.key;
            }

            public final Comparator<? super K> getComparator() {
                return SubMap.this.comparator();
            }
        }

        final class SubMapEntryIterator extends SubMap<K, V>.SubMapIter<Map.Entry<K, V>> {
            public int characteristics() {
                return 1;
            }

            SubMapEntryIterator() {
                super();
            }

            public Map.Entry<K, V> next() {
                Node node = this.next;
                Object obj = this.nextValue;
                advance();
                return new AbstractMap.SimpleImmutableEntry(node.key, obj);
            }
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        biConsumer.getClass();
        Node<K, V> baseHead = baseHead();
        if (baseHead != null) {
            while (true) {
                baseHead = baseHead.next;
                if (baseHead != null) {
                    V v = baseHead.val;
                    if (v != null) {
                        biConsumer.accept(baseHead.key, v);
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean removeEntryIf(Predicate<? super Map.Entry<K, V>> predicate) {
        predicate.getClass();
        Node<K, V> baseHead = baseHead();
        boolean z = false;
        if (baseHead != null) {
            while (true) {
                baseHead = baseHead.next;
                if (baseHead == null) {
                    break;
                }
                V v = baseHead.val;
                if (v != null) {
                    K k = baseHead.key;
                    if (predicate.test(new AbstractMap.SimpleImmutableEntry(k, v)) && remove(k, v)) {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public boolean removeValueIf(Predicate<? super V> predicate) {
        predicate.getClass();
        Node<K, V> baseHead = baseHead();
        boolean z = false;
        if (baseHead != null) {
            while (true) {
                baseHead = baseHead.next;
                if (baseHead == null) {
                    break;
                }
                V v = baseHead.val;
                if (v != null && predicate.test(v) && remove(baseHead.key, v)) {
                    z = true;
                }
            }
        }
        return z;
    }

    static abstract class CSLMSpliterator<K, V> {
        final Comparator<? super K> comparator;
        Node<K, V> current;
        long est;
        final K fence;
        Index<K, V> row;

        CSLMSpliterator(Comparator<? super K> comparator2, Index<K, V> index, Node<K, V> node, K k, long j) {
            this.comparator = comparator2;
            this.row = index;
            this.current = node;
            this.fence = k;
            this.est = j;
        }

        public final long estimateSize() {
            return this.est;
        }
    }

    static final class KeySpliterator<K, V> extends CSLMSpliterator<K, V> implements Spliterator<K> {
        public int characteristics() {
            return 4373;
        }

        KeySpliterator(Comparator<? super K> comparator, Index<K, V> index, Node<K, V> node, K k, long j) {
            super(comparator, index, node, k, j);
        }

        public KeySpliterator<K, V> trySplit() {
            K k;
            Node<K, V> node;
            Node<K, V> node2;
            K k2;
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node node3 = this.current;
            if (node3 == null || (k = node3.key) == null) {
                return null;
            }
            Index<K, V> index = this.row;
            while (index != null) {
                Index<K, V> index2 = index.right;
                if (index2 == null || (node = index2.node) == null || (node2 = node.next) == null || node2.val == null || (k2 = node2.key) == null || ConcurrentSkipListMap.cpr(comparator, k2, k) <= 0 || (obj != null && ConcurrentSkipListMap.cpr(comparator, k2, obj) >= 0)) {
                    index = index.down;
                    this.row = index;
                } else {
                    this.current = node2;
                    Index<K, V> index3 = index.down;
                    if (index2.right == null) {
                        index2 = index2.down;
                    }
                    this.row = index2;
                    this.est -= this.est >>> 2;
                    return new KeySpliterator(comparator, index3, node3, k2, this.est);
                }
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super K> consumer) {
            consumer.getClass();
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node<K, V> node = this.current;
            this.current = null;
            while (node != null) {
                K k = node.key;
                if (k == null || obj == null || ConcurrentSkipListMap.cpr(comparator, obj, k) > 0) {
                    if (node.val != null) {
                        consumer.accept(k);
                    }
                    node = node.next;
                } else {
                    return;
                }
            }
        }

        public boolean tryAdvance(Consumer<? super K> consumer) {
            consumer.getClass();
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node<K, V> node = this.current;
            while (true) {
                if (node != null) {
                    K k = node.key;
                    if (k != null && obj != null && ConcurrentSkipListMap.cpr(comparator, obj, k) <= 0) {
                        node = null;
                        break;
                    } else if (node.val != null) {
                        this.current = node.next;
                        consumer.accept(k);
                        return true;
                    } else {
                        node = node.next;
                    }
                } else {
                    break;
                }
            }
            this.current = node;
            return false;
        }

        public final Comparator<? super K> getComparator() {
            return this.comparator;
        }
    }

    /* access modifiers changed from: package-private */
    public final KeySpliterator<K, V> keySpliterator() {
        long j;
        Node<K, V> node;
        VarHandle.acquireFence();
        Index<K, V> index = this.head;
        if (index == null) {
            node = null;
            j = 0;
        } else {
            node = index.node;
            j = getAdderCount();
        }
        long j2 = j;
        return new KeySpliterator(this.comparator, index, node, null, j2);
    }

    static final class ValueSpliterator<K, V> extends CSLMSpliterator<K, V> implements Spliterator<V> {
        public int characteristics() {
            return 4368;
        }

        ValueSpliterator(Comparator<? super K> comparator, Index<K, V> index, Node<K, V> node, K k, long j) {
            super(comparator, index, node, k, j);
        }

        public ValueSpliterator<K, V> trySplit() {
            K k;
            Node<K, V> node;
            Node<K, V> node2;
            K k2;
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node node3 = this.current;
            if (node3 == null || (k = node3.key) == null) {
                return null;
            }
            Index<K, V> index = this.row;
            while (index != null) {
                Index<K, V> index2 = index.right;
                if (index2 == null || (node = index2.node) == null || (node2 = node.next) == null || node2.val == null || (k2 = node2.key) == null || ConcurrentSkipListMap.cpr(comparator, k2, k) <= 0 || (obj != null && ConcurrentSkipListMap.cpr(comparator, k2, obj) >= 0)) {
                    index = index.down;
                    this.row = index;
                } else {
                    this.current = node2;
                    Index<K, V> index3 = index.down;
                    if (index2.right == null) {
                        index2 = index2.down;
                    }
                    this.row = index2;
                    this.est -= this.est >>> 2;
                    return new ValueSpliterator(comparator, index3, node3, k2, this.est);
                }
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super V> consumer) {
            consumer.getClass();
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node<K, V> node = this.current;
            this.current = null;
            while (node != null) {
                K k = node.key;
                if (k == null || obj == null || ConcurrentSkipListMap.cpr(comparator, obj, k) > 0) {
                    V v = node.val;
                    if (v != null) {
                        consumer.accept(v);
                    }
                    node = node.next;
                } else {
                    return;
                }
            }
        }

        public boolean tryAdvance(Consumer<? super V> consumer) {
            consumer.getClass();
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node<K, V> node = this.current;
            while (true) {
                if (node != null) {
                    K k = node.key;
                    if (k != null && obj != null && ConcurrentSkipListMap.cpr(comparator, obj, k) <= 0) {
                        node = null;
                        break;
                    }
                    V v = node.val;
                    if (v != null) {
                        this.current = node.next;
                        consumer.accept(v);
                        return true;
                    }
                    node = node.next;
                } else {
                    break;
                }
            }
            this.current = node;
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public final ValueSpliterator<K, V> valueSpliterator() {
        long j;
        Node<K, V> node;
        VarHandle.acquireFence();
        Index<K, V> index = this.head;
        if (index == null) {
            node = null;
            j = 0;
        } else {
            node = index.node;
            j = getAdderCount();
        }
        long j2 = j;
        return new ValueSpliterator(this.comparator, index, node, null, j2);
    }

    static final class EntrySpliterator<K, V> extends CSLMSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        public int characteristics() {
            return 4373;
        }

        EntrySpliterator(Comparator<? super K> comparator, Index<K, V> index, Node<K, V> node, K k, long j) {
            super(comparator, index, node, k, j);
        }

        public EntrySpliterator<K, V> trySplit() {
            K k;
            Node<K, V> node;
            Node<K, V> node2;
            K k2;
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node node3 = this.current;
            if (node3 == null || (k = node3.key) == null) {
                return null;
            }
            Index<K, V> index = this.row;
            while (index != null) {
                Index<K, V> index2 = index.right;
                if (index2 == null || (node = index2.node) == null || (node2 = node.next) == null || node2.val == null || (k2 = node2.key) == null || ConcurrentSkipListMap.cpr(comparator, k2, k) <= 0 || (obj != null && ConcurrentSkipListMap.cpr(comparator, k2, obj) >= 0)) {
                    index = index.down;
                    this.row = index;
                } else {
                    this.current = node2;
                    Index<K, V> index3 = index.down;
                    if (index2.right == null) {
                        index2 = index2.down;
                    }
                    this.row = index2;
                    this.est -= this.est >>> 2;
                    return new EntrySpliterator(comparator, index3, node3, k2, this.est);
                }
            }
            return null;
        }

        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node<K, V> node = this.current;
            this.current = null;
            while (node != null) {
                K k = node.key;
                if (k == null || obj == null || ConcurrentSkipListMap.cpr(comparator, obj, k) > 0) {
                    V v = node.val;
                    if (v != null) {
                        consumer.accept(new AbstractMap.SimpleImmutableEntry(k, v));
                    }
                    node = node.next;
                } else {
                    return;
                }
            }
        }

        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            consumer.getClass();
            Comparator comparator = this.comparator;
            Object obj = this.fence;
            Node<K, V> node = this.current;
            while (true) {
                if (node != null) {
                    K k = node.key;
                    if (k != null && obj != null && ConcurrentSkipListMap.cpr(comparator, obj, k) <= 0) {
                        node = null;
                        break;
                    }
                    V v = node.val;
                    if (v != null) {
                        this.current = node.next;
                        consumer.accept(new AbstractMap.SimpleImmutableEntry(k, v));
                        return true;
                    }
                    node = node.next;
                } else {
                    break;
                }
            }
            this.current = node;
            return false;
        }

        public final Comparator<Map.Entry<K, V>> getComparator() {
            if (this.comparator != null) {
                return Map.Entry.comparingByKey(this.comparator);
            }
            return new ConcurrentSkipListMap$EntrySpliterator$$ExternalSyntheticLambda0();
        }
    }

    /* access modifiers changed from: package-private */
    public final EntrySpliterator<K, V> entrySpliterator() {
        long j;
        Node<K, V> node;
        VarHandle.acquireFence();
        Index<K, V> index = this.head;
        if (index == null) {
            node = null;
            j = 0;
        } else {
            node = index.node;
            j = getAdderCount();
        }
        long j2 = j;
        return new EntrySpliterator(this.comparator, index, node, null, j2);
    }

    static {
        Class<ConcurrentSkipListMap> cls = ConcurrentSkipListMap.class;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            HEAD = lookup.findVarHandle(cls, "head", Index.class);
            ADDER = lookup.findVarHandle(cls, "adder", LongAdder.class);
            NEXT = lookup.findVarHandle(Node.class, "next", Node.class);
            VAL = lookup.findVarHandle(Node.class, "val", Object.class);
            RIGHT = lookup.findVarHandle(Index.class, NavigationBarInflaterView.RIGHT, Index.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
