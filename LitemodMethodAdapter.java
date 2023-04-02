package org.funnycoin;


import jdk.internal.org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;

public class LitemodMethodAdapter {
    private void changePermissibleModName(String newModName) throws IOException {
        ClassReader reader = new ClassReader("net.eq2online.macros.LiteModMacros");
        ClassWriter writer = new ClassWriter(reader,ClassWriter.COMPUTE_MAXS);
        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM5,writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mVisitor = super.visitMethod(access,name,descriptor,signature,exceptions);
                return new MethodVisitor(Opcodes.ASM5,mVisitor) {
                    @Override
                    public void visitInsn(int opcode) {
                        if(opcode == Opcodes.RETURN) {
                            mVisitor.visitLdcInsn(newModName);
                        }
                        super.visitInsn(opcode);
                    }
                };
            }
        };
        reader.accept(visitor,ClassReader.EXPAND_FRAMES);
        byte[] classData = writer.toByteArray();
        // write this to the class
    }
}
