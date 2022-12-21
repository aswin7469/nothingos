package java.util.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.IOException;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;

/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.utils.BlockUtils.isAllBlocksEmpty(BlockUtils.java:564)
    	at jadx.core.dex.visitors.ExtractFieldInit.getConstructorsList(ExtractFieldInit.java:245)
    	at jadx.core.dex.visitors.ExtractFieldInit.moveCommonFieldsInit(ExtractFieldInit.java:126)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:46)
    */
public class ConcurrentLinkedDeque<E> extends AbstractCollection<E> implements Deque<E>, Serializable {
    private static final VarHandle HEAD;
    private static final int HOPS = 2;
    private static final VarHandle ITEM;
    private static final VarHandle NEXT;
    private static final Node<Object> NEXT_TERMINATOR;
    private static final VarHandle PREV;
    private static final Node<Object> PREV_TERMINATOR;
    private static final VarHandle TAIL;
    private static final long serialVersionUID = 876323262645176354L;
    private volatile transient Node<E> head;
    private volatile transient Node<E> tail;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.<init>(java.util.Collection):void, dex: classes4.dex
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
    public ConcurrentLinkedDeque(java.util.Collection<? extends E> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.<init>(java.util.Collection):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.<init>(java.util.Collection):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.bulkRemove(java.util.function.Predicate):boolean, dex: classes4.dex
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
    private boolean bulkRemove(java.util.function.Predicate<? super E> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.bulkRemove(java.util.function.Predicate):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.bulkRemove(java.util.function.Predicate):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.initHeadTail(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
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
    private void initHeadTail(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r1, java.util.concurrent.ConcurrentLinkedDeque.Node<E> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.initHeadTail(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.initHeadTail(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.linkFirst(java.lang.Object):void, dex: classes4.dex
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
    private void linkFirst(E r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.linkFirst(java.lang.Object):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.linkFirst(java.lang.Object):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.linkLast(java.lang.Object):void, dex: classes4.dex
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
    private void linkLast(E r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.linkLast(java.lang.Object):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.linkLast(java.lang.Object):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.newNode(java.lang.Object):java.util.concurrent.ConcurrentLinkedDeque$Node<E>, dex: classes4.dex
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
    static <E> java.util.concurrent.ConcurrentLinkedDeque.Node<E> newNode(E r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.newNode(java.lang.Object):java.util.concurrent.ConcurrentLinkedDeque$Node<E>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.newNode(java.lang.Object):java.util.concurrent.ConcurrentLinkedDeque$Node");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.readObject(java.io.ObjectInputStream):void, dex: classes4.dex
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
    private void readObject(java.p026io.ObjectInputStream r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.readObject(java.io.ObjectInputStream):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.readObject(java.io.ObjectInputStream):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
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
    private void skipDeletedPredecessors(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
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
    private void skipDeletedSuccessors(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors(java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.unlinkFirst(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
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
    private void unlinkFirst(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r1, java.util.concurrent.ConcurrentLinkedDeque.Node<E> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.unlinkFirst(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.unlinkFirst(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.unlinkLast(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
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
    private void unlinkLast(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r1, java.util.concurrent.ConcurrentLinkedDeque.Node<E> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.unlinkLast(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.unlinkLast(java.util.concurrent.ConcurrentLinkedDeque$Node, java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.updateHead():void, dex: classes4.dex
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
    private final void updateHead() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.updateHead():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.updateHead():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.updateTail():void, dex: classes4.dex
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
    private final void updateTail() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.updateTail():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.updateTail():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.addAll(java.util.Collection):boolean, dex: classes4.dex
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
    public boolean addAll(java.util.Collection<? extends E> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.addAll(java.util.Collection):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.addAll(java.util.Collection):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.first():java.util.concurrent.ConcurrentLinkedDeque$Node<E>, dex: classes4.dex
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
    java.util.concurrent.ConcurrentLinkedDeque.Node<E> first() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.first():java.util.concurrent.ConcurrentLinkedDeque$Node<E>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.first():java.util.concurrent.ConcurrentLinkedDeque$Node");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.last():java.util.concurrent.ConcurrentLinkedDeque$Node<E>, dex: classes4.dex
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
    java.util.concurrent.ConcurrentLinkedDeque.Node<E> last() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.last():java.util.concurrent.ConcurrentLinkedDeque$Node<E>, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.last():java.util.concurrent.ConcurrentLinkedDeque$Node");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.pollFirst():E, dex: classes4.dex
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
    public E pollFirst() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.pollFirst():E, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.pollFirst():java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.pollLast():E, dex: classes4.dex
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
    public E pollLast() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.pollLast():E, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.pollLast():java.lang.Object");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.removeFirstOccurrence(java.lang.Object):boolean, dex: classes4.dex
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
    public boolean removeFirstOccurrence(java.lang.Object r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.removeFirstOccurrence(java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.removeFirstOccurrence(java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.removeLastOccurrence(java.lang.Object):boolean, dex: classes4.dex
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
    public boolean removeLastOccurrence(java.lang.Object r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.removeLastOccurrence(java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.removeLastOccurrence(java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.unlink(java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
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
    void unlink(java.util.concurrent.ConcurrentLinkedDeque.Node<E> r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ConcurrentLinkedDeque.unlink(java.util.concurrent.ConcurrentLinkedDeque$Node):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedDeque.unlink(java.util.concurrent.ConcurrentLinkedDeque$Node):void");
    }

    /* access modifiers changed from: package-private */
    public Node<E> prevTerminator() {
        return PREV_TERMINATOR;
    }

    /* access modifiers changed from: package-private */
    public Node<E> nextTerminator() {
        return NEXT_TERMINATOR;
    }

    static final class Node<E> {
        volatile E item;
        volatile Node<E> next;
        volatile Node<E> prev;

        Node() {
        }
    }

    /* access modifiers changed from: package-private */
    public final Node<E> succ(Node<E> node) {
        Node<E> node2 = node.next;
        return node == node2 ? first() : node2;
    }

    /* access modifiers changed from: package-private */
    public final Node<E> pred(Node<E> node) {
        Node<E> node2 = node.prev;
        return node == node2 ? last() : node2;
    }

    private E screenNullResult(E e) {
        if (e != null) {
            return e;
        }
        throw new NoSuchElementException();
    }

    public ConcurrentLinkedDeque() {
        Node<E> node = new Node<>();
        this.tail = node;
        this.head = node;
    }

    public void addFirst(E e) {
        linkFirst(e);
    }

    public void addLast(E e) {
        linkLast(e);
    }

    public boolean offerFirst(E e) {
        linkFirst(e);
        return true;
    }

    public boolean offerLast(E e) {
        linkLast(e);
        return true;
    }

    public E peekFirst() {
        E e;
        while (true) {
            Node<E> first = first();
            Node<E> node = first;
            while (true) {
                e = node.item;
                if (e == null) {
                    Node<E> node2 = node.next;
                    if (node == node2) {
                        continue;
                        break;
                    } else if (node2 == null) {
                        break;
                    } else {
                        node = node2;
                    }
                } else {
                    break;
                }
            }
            if (first.prev == null) {
                return e;
            }
        }
    }

    public E peekLast() {
        E e;
        while (true) {
            Node<E> last = last();
            Node<E> node = last;
            while (true) {
                e = node.item;
                if (e == null) {
                    Node<E> node2 = node.prev;
                    if (node == node2) {
                        continue;
                        break;
                    } else if (node2 == null) {
                        break;
                    } else {
                        node = node2;
                    }
                } else {
                    break;
                }
            }
            if (last.next == null) {
                return e;
            }
        }
    }

    public E getFirst() {
        return screenNullResult(peekFirst());
    }

    public E getLast() {
        return screenNullResult(peekLast());
    }

    public E removeFirst() {
        return screenNullResult(pollFirst());
    }

    public E removeLast() {
        return screenNullResult(pollLast());
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public boolean add(E e) {
        return offerLast(e);
    }

    public E poll() {
        return pollFirst();
    }

    public E peek() {
        return peekFirst();
    }

    public E remove() {
        return removeFirst();
    }

    public E pop() {
        return removeFirst();
    }

    public E element() {
        return getFirst();
    }

    public void push(E e) {
        addFirst(e);
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Node first = first();
        while (first != null) {
            E e = first.item;
            if (e != null && obj.equals(e)) {
                return true;
            }
            first = succ(first);
        }
        return false;
    }

    public boolean isEmpty() {
        return peekFirst() == null;
    }

    public int size() {
        int i;
        loop0:
        while (true) {
            Node<E> first = first();
            i = 0;
            while (true) {
                if (first == null || (first.item != null && (i = i + 1) == Integer.MAX_VALUE)) {
                    return i;
                }
                Node<E> node = first.next;
                if (first != node) {
                    first = node;
                }
            }
        }
        return i;
    }

    public boolean remove(Object obj) {
        return removeFirstOccurrence(obj);
    }

    public void clear() {
        do {
        } while (pollFirst() != null);
    }

    public String toString() {
        int i;
        int i2;
        String[] strArr = null;
        loop0:
        while (true) {
            Node<E> first = first();
            i = 0;
            i2 = 0;
            while (true) {
                if (first == null) {
                    break loop0;
                }
                E e = first.item;
                if (e != null) {
                    if (strArr == null) {
                        strArr = new String[4];
                    } else if (i == strArr.length) {
                        strArr = (String[]) Arrays.copyOf((T[]) strArr, i * 2);
                    }
                    String obj = e.toString();
                    strArr[i] = obj;
                    i2 += obj.length();
                    i++;
                }
                Node<E> node = first.next;
                if (first != node) {
                    first = node;
                }
            }
        }
        if (i == 0) {
            return "[]";
        }
        return Helpers.toString(strArr, i, i2);
    }

    private Object[] toArrayInternal(Object[] objArr) {
        int i;
        Object[] objArr2 = objArr;
        loop0:
        while (true) {
            Node<E> first = first();
            i = 0;
            while (true) {
                if (first == null) {
                    break loop0;
                }
                E e = first.item;
                if (e != null) {
                    if (objArr2 == null) {
                        objArr2 = new Object[4];
                    } else if (i == objArr2.length) {
                        objArr2 = Arrays.copyOf((T[]) objArr2, (i + 4) * 2);
                    }
                    objArr2[i] = e;
                    i++;
                }
                Node<E> node = first.next;
                if (first != node) {
                    first = node;
                }
            }
        }
        if (objArr2 == null) {
            return new Object[0];
        }
        if (objArr == null || i > objArr.length) {
            return i == objArr2.length ? objArr2 : Arrays.copyOf((T[]) objArr2, i);
        }
        if (objArr != objArr2) {
            System.arraycopy((Object) objArr2, 0, (Object) objArr, 0, i);
        }
        if (i < objArr.length) {
            objArr[i] = null;
        }
        return objArr;
    }

    public Object[] toArray() {
        return toArrayInternal((Object[]) null);
    }

    public <T> T[] toArray(T[] tArr) {
        tArr.getClass();
        return toArrayInternal(tArr);
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public Iterator<E> descendingIterator() {
        return new DescendingItr();
    }

    private abstract class AbstractItr implements Iterator<E> {
        private Node<E> lastRet;
        private E nextItem;
        private Node<E> nextNode;

        /* access modifiers changed from: package-private */
        public abstract Node<E> nextNode(Node<E> node);

        /* access modifiers changed from: package-private */
        public abstract Node<E> startNode();

        AbstractItr() {
            advance();
        }

        private void advance() {
            Node<E> node = this.nextNode;
            this.lastRet = node;
            Node<E> startNode = node == null ? startNode() : nextNode(node);
            while (startNode != null) {
                E e = startNode.item;
                if (e != null) {
                    this.nextNode = startNode;
                    this.nextItem = e;
                    return;
                }
                startNode = nextNode(startNode);
            }
            this.nextNode = null;
            this.nextItem = null;
        }

        public boolean hasNext() {
            return this.nextItem != null;
        }

        public E next() {
            E e = this.nextItem;
            if (e != null) {
                advance();
                return e;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            Node<E> node = this.lastRet;
            if (node != null) {
                node.item = null;
                ConcurrentLinkedDeque.this.unlink(node);
                this.lastRet = null;
                return;
            }
            throw new IllegalStateException();
        }
    }

    private class Itr extends ConcurrentLinkedDeque<E>.AbstractItr {
        Itr() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Node<E> startNode() {
            return ConcurrentLinkedDeque.this.first();
        }

        /* access modifiers changed from: package-private */
        public Node<E> nextNode(Node<E> node) {
            return ConcurrentLinkedDeque.this.succ(node);
        }
    }

    private class DescendingItr extends ConcurrentLinkedDeque<E>.AbstractItr {
        DescendingItr() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Node<E> startNode() {
            return ConcurrentLinkedDeque.this.last();
        }

        /* access modifiers changed from: package-private */
        public Node<E> nextNode(Node<E> node) {
            return ConcurrentLinkedDeque.this.pred(node);
        }
    }

    final class CLDSpliterator implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        int batch;
        Node<E> current;
        boolean exhausted;

        public int characteristics() {
            return 4368;
        }

        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        CLDSpliterator() {
        }

        public Spliterator<E> trySplit() {
            Node<E> node;
            Node<E> current2 = current();
            if (current2 == null || (node = current2.next) == null) {
                return null;
            }
            int min = Math.min(this.batch + 1, 33554432);
            this.batch = min;
            Object[] objArr = null;
            int i = 0;
            do {
                E e = current2.item;
                if (e != null) {
                    if (objArr == null) {
                        objArr = new Object[min];
                    }
                    objArr[i] = e;
                    i++;
                }
                current2 = current2 == node ? ConcurrentLinkedDeque.this.first() : node;
                if (current2 == null || (node = current2.next) == null) {
                    setCurrent(current2);
                }
            } while (i < min);
            setCurrent(current2);
            if (i == 0) {
                return null;
            }
            return Spliterators.spliterator(objArr, 0, i, 4368);
        }

        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            Node<E> current2 = current();
            if (current2 != null) {
                this.current = null;
                this.exhausted = true;
                do {
                    E e = current2.item;
                    if (e != null) {
                        consumer.accept(e);
                    }
                    Node<E> node = current2.next;
                    if (current2 == node) {
                        current2 = ConcurrentLinkedDeque.this.first();
                        continue;
                    } else {
                        current2 = node;
                        continue;
                    }
                } while (current2 != null);
            }
        }

        public boolean tryAdvance(Consumer<? super E> consumer) {
            E e;
            Objects.requireNonNull(consumer);
            Node<E> current2 = current();
            if (current2 == null) {
                return false;
            }
            do {
                e = current2.item;
                Node<E> node = current2.next;
                current2 = current2 == node ? ConcurrentLinkedDeque.this.first() : node;
                if (e != null) {
                    break;
                }
            } while (current2 != null);
            setCurrent(current2);
            if (e == null) {
                return false;
            }
            consumer.accept(e);
            return true;
        }

        private void setCurrent(Node<E> node) {
            this.current = node;
            if (node == null) {
                this.exhausted = true;
            }
        }

        private Node<E> current() {
            Node<E> node = this.current;
            if (node != null || this.exhausted) {
                return node;
            }
            Node<E> first = ConcurrentLinkedDeque.this.first();
            setCurrent(first);
            return first;
        }
    }

    public Spliterator<E> spliterator() {
        return new CLDSpliterator();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Node first = first();
        while (first != null) {
            E e = first.item;
            if (e != null) {
                objectOutputStream.writeObject(e);
            }
            first = succ(first);
        }
        objectOutputStream.writeObject((Object) null);
    }

    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return bulkRemove(predicate);
    }

    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new ConcurrentLinkedDeque$$ExternalSyntheticLambda0(collection));
    }

    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return bulkRemove(new ConcurrentLinkedDeque$$ExternalSyntheticLambda1(collection));
    }

    static /* synthetic */ boolean lambda$retainAll$1(Collection collection, Object obj) {
        return !collection.contains(obj);
    }

    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        Node first = first();
        while (first != null) {
            E e = first.item;
            if (e != null) {
                consumer.accept(e);
            }
            first = succ(first);
        }
    }

    static {
        Class<ConcurrentLinkedDeque> cls = ConcurrentLinkedDeque.class;
        Node<Object> node = new Node<>();
        PREV_TERMINATOR = node;
        node.next = node;
        Node<Object> node2 = new Node<>();
        NEXT_TERMINATOR = node2;
        node2.prev = node2;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            HEAD = lookup.findVarHandle(cls, "head", Node.class);
            TAIL = lookup.findVarHandle(cls, "tail", Node.class);
            PREV = lookup.findVarHandle(Node.class, "prev", Node.class);
            NEXT = lookup.findVarHandle(Node.class, "next", Node.class);
            ITEM = lookup.findVarHandle(Node.class, "item", Object.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
