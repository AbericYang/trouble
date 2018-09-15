/*
 * MIT License
 *
 * Copyright (c) 2018 Aberic Yang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: enums/TransactionStatus.proto

package cn.aberic.bother.entity.proto.enums;

public final class TransactionStatusProto {
  private TransactionStatusProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * <pre>
   * 交易状态
   * </pre>
   *
   * Protobuf enum {@code TransactionStatus}
   */
  public enum TransactionStatus
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     * 交易成功
     * </pre>
     *
     * <code>SUCCESS = 0;</code>
     */
    SUCCESS(0),
    /**
     * <pre>
     * 交易失败
     * </pre>
     *
     * <code>FAIL = 1;</code>
     */
    FAIL(1),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     * 交易成功
     * </pre>
     *
     * <code>SUCCESS = 0;</code>
     */
    public static final int SUCCESS_VALUE = 0;
    /**
     * <pre>
     * 交易失败
     * </pre>
     *
     * <code>FAIL = 1;</code>
     */
    public static final int FAIL_VALUE = 1;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @Deprecated
    public static TransactionStatus valueOf(int value) {
      return forNumber(value);
    }

    public static TransactionStatus forNumber(int value) {
      switch (value) {
        case 0: return SUCCESS;
        case 1: return FAIL;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<TransactionStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        TransactionStatus> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<TransactionStatus>() {
            public TransactionStatus findValueByNumber(int number) {
              return TransactionStatus.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return TransactionStatusProto.getDescriptor().getEnumTypes().get(0);
    }

    private static final TransactionStatus[] VALUES = values();

    public static TransactionStatus valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private TransactionStatus(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:TransactionStatus)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\035enums/TransactionStatus.proto**\n\021Trans" +
      "actionStatus\022\013\n\007SUCCESS\020\000\022\010\n\004FAIL\020\001B=\n#c" +
      "n.aberic.bother.entity.proto.enumsB\026Tran" +
      "sactionStatusProtob\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
