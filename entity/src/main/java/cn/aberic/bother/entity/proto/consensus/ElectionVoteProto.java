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
// source: consensus/ElectionVote.proto

package cn.aberic.bother.entity.proto.consensus;

public final class ElectionVoteProto {
  private ElectionVoteProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ElectionVoteOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ElectionVote)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 本次投票节点地址
     * </pre>
     *
     * <code>string address = 1;</code>
     */
    String getAddress();
    /**
     * <pre>
     * 本次投票节点地址
     * </pre>
     *
     * <code>string address = 1;</code>
     */
    com.google.protobuf.ByteString
        getAddressBytes();

    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    java.util.List<String>
        getAddressesList();
    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    int getAddressesCount();
    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    String getAddresses(int index);
    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    com.google.protobuf.ByteString
        getAddressesBytes(int index);
  }
  /**
   * <pre>
   * 节点发起选举投票对象
   * </pre>
   *
   * Protobuf type {@code ElectionVote}
   */
  public  static final class ElectionVote extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ElectionVote)
      ElectionVoteOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use ElectionVote.newBuilder() to construct.
    private ElectionVote(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ElectionVote() {
      address_ = "";
      addresses_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private ElectionVote(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              String s = input.readStringRequireUtf8();

              address_ = s;
              break;
            }
            case 18: {
              String s = input.readStringRequireUtf8();
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                addresses_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000002;
              }
              addresses_.add(s);
              break;
            }
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          addresses_ = addresses_.getUnmodifiableView();
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ElectionVoteProto.internal_static_ElectionVote_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ElectionVoteProto.internal_static_ElectionVote_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ElectionVoteProto.ElectionVote.class, ElectionVoteProto.ElectionVote.Builder.class);
    }

    private int bitField0_;
    public static final int ADDRESS_FIELD_NUMBER = 1;
    private volatile Object address_;
    /**
     * <pre>
     * 本次投票节点地址
     * </pre>
     *
     * <code>string address = 1;</code>
     */
    public String getAddress() {
      Object ref = address_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        address_ = s;
        return s;
      }
    }
    /**
     * <pre>
     * 本次投票节点地址
     * </pre>
     *
     * <code>string address = 1;</code>
     */
    public com.google.protobuf.ByteString
        getAddressBytes() {
      Object ref = address_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        address_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int ADDRESSES_FIELD_NUMBER = 2;
    private com.google.protobuf.LazyStringList addresses_;
    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getAddressesList() {
      return addresses_;
    }
    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    public int getAddressesCount() {
      return addresses_.size();
    }
    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    public String getAddresses(int index) {
      return addresses_.get(index);
    }
    /**
     * <pre>
     * 本次投票节点结果集合
     * </pre>
     *
     * <code>repeated string addresses = 2;</code>
     */
    public com.google.protobuf.ByteString
        getAddressesBytes(int index) {
      return addresses_.getByteString(index);
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (!getAddressBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 1, address_);
      }
      for (int i = 0; i < addresses_.size(); i++) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 2, addresses_.getRaw(i));
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (!getAddressBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, address_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < addresses_.size(); i++) {
          dataSize += computeStringSizeNoTag(addresses_.getRaw(i));
        }
        size += dataSize;
        size += 1 * getAddressesList().size();
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof ElectionVoteProto.ElectionVote)) {
        return super.equals(obj);
      }
      ElectionVoteProto.ElectionVote other = (ElectionVoteProto.ElectionVote) obj;

      boolean result = true;
      result = result && getAddress()
          .equals(other.getAddress());
      result = result && getAddressesList()
          .equals(other.getAddressesList());
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      hash = (37 * hash) + ADDRESS_FIELD_NUMBER;
      hash = (53 * hash) + getAddress().hashCode();
      if (getAddressesCount() > 0) {
        hash = (37 * hash) + ADDRESSES_FIELD_NUMBER;
        hash = (53 * hash) + getAddressesList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ElectionVoteProto.ElectionVote parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ElectionVoteProto.ElectionVote parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ElectionVoteProto.ElectionVote parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ElectionVoteProto.ElectionVote parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(ElectionVoteProto.ElectionVote prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * <pre>
     * 节点发起选举投票对象
     * </pre>
     *
     * Protobuf type {@code ElectionVote}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ElectionVote)
        ElectionVoteProto.ElectionVoteOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ElectionVoteProto.internal_static_ElectionVote_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ElectionVoteProto.internal_static_ElectionVote_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ElectionVoteProto.ElectionVote.class, ElectionVoteProto.ElectionVote.Builder.class);
      }

      // Construct using cn.aberic.bother.entity.proto.consensus.ElectionVoteProto.ElectionVote.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        address_ = "";

        addresses_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ElectionVoteProto.internal_static_ElectionVote_descriptor;
      }

      @Override
      public ElectionVoteProto.ElectionVote getDefaultInstanceForType() {
        return ElectionVoteProto.ElectionVote.getDefaultInstance();
      }

      @Override
      public ElectionVoteProto.ElectionVote build() {
        ElectionVoteProto.ElectionVote result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public ElectionVoteProto.ElectionVote buildPartial() {
        ElectionVoteProto.ElectionVote result = new ElectionVoteProto.ElectionVote(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        result.address_ = address_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          addresses_ = addresses_.getUnmodifiableView();
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.addresses_ = addresses_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      @Override
      public Builder clone() {
        return (Builder) super.clone();
      }
      @Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      @Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      @Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      @Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      @Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ElectionVoteProto.ElectionVote) {
          return mergeFrom((ElectionVoteProto.ElectionVote)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ElectionVoteProto.ElectionVote other) {
        if (other == ElectionVoteProto.ElectionVote.getDefaultInstance()) return this;
        if (!other.getAddress().isEmpty()) {
          address_ = other.address_;
          onChanged();
        }
        if (!other.addresses_.isEmpty()) {
          if (addresses_.isEmpty()) {
            addresses_ = other.addresses_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureAddressesIsMutable();
            addresses_.addAll(other.addresses_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @Override
      public final boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ElectionVoteProto.ElectionVote parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ElectionVoteProto.ElectionVote) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private Object address_ = "";
      /**
       * <pre>
       * 本次投票节点地址
       * </pre>
       *
       * <code>string address = 1;</code>
       */
      public String getAddress() {
        Object ref = address_;
        if (!(ref instanceof String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          String s = bs.toStringUtf8();
          address_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      /**
       * <pre>
       * 本次投票节点地址
       * </pre>
       *
       * <code>string address = 1;</code>
       */
      public com.google.protobuf.ByteString
          getAddressBytes() {
        Object ref = address_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b =
              com.google.protobuf.ByteString.copyFromUtf8(
                  (String) ref);
          address_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <pre>
       * 本次投票节点地址
       * </pre>
       *
       * <code>string address = 1;</code>
       */
      public Builder setAddress(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }

        address_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 本次投票节点地址
       * </pre>
       *
       * <code>string address = 1;</code>
       */
      public Builder clearAddress() {

        address_ = getDefaultInstance().getAddress();
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 本次投票节点地址
       * </pre>
       *
       * <code>string address = 1;</code>
       */
      public Builder setAddressBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);

        address_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.LazyStringList addresses_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureAddressesIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          addresses_ = new com.google.protobuf.LazyStringArrayList(addresses_);
          bitField0_ |= 0x00000002;
         }
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public com.google.protobuf.ProtocolStringList
          getAddressesList() {
        return addresses_.getUnmodifiableView();
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public int getAddressesCount() {
        return addresses_.size();
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public String getAddresses(int index) {
        return addresses_.get(index);
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public com.google.protobuf.ByteString
          getAddressesBytes(int index) {
        return addresses_.getByteString(index);
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public Builder setAddresses(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureAddressesIsMutable();
        addresses_.set(index, value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public Builder addAddresses(
          String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureAddressesIsMutable();
        addresses_.add(value);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public Builder addAllAddresses(
          Iterable<String> values) {
        ensureAddressesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, addresses_);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public Builder clearAddresses() {
        addresses_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 本次投票节点结果集合
       * </pre>
       *
       * <code>repeated string addresses = 2;</code>
       */
      public Builder addAddressesBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        ensureAddressesIsMutable();
        addresses_.add(value);
        onChanged();
        return this;
      }
      @Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      @Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:ElectionVote)
    }

    // @@protoc_insertion_point(class_scope:ElectionVote)
    private static final ElectionVoteProto.ElectionVote DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ElectionVoteProto.ElectionVote();
    }

    public static ElectionVoteProto.ElectionVote getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ElectionVote>
        PARSER = new com.google.protobuf.AbstractParser<ElectionVote>() {
      @Override
      public ElectionVote parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ElectionVote(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ElectionVote> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<ElectionVote> getParserForType() {
      return PARSER;
    }

    @Override
    public ElectionVoteProto.ElectionVote getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ElectionVote_descriptor;
  private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ElectionVote_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\034consensus/ElectionVote.proto\"2\n\014Electi" +
      "onVote\022\017\n\007address\030\001 \001(\t\022\021\n\taddresses\030\002 \003" +
      "(\tB<\n\'cn.aberic.bother.entity.proto.cons" +
      "ensusB\021ElectionVoteProtob\006proto3"
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
    internal_static_ElectionVote_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ElectionVote_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ElectionVote_descriptor,
        new String[] { "Address", "Addresses", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
