// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Block.proto

package cn.aberic.bother.entity.proto;

public final class BlockProto {
  private BlockProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface BlockOrBuilder extends
      // @@protoc_insertion_point(interface_extends:Block)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 区块头部信息
     * </pre>
     *
     * <code>.BlockHeader header = 1;</code>
     */
    boolean hasHeader();
    /**
     * <pre>
     * 区块头部信息
     * </pre>
     *
     * <code>.BlockHeader header = 1;</code>
     */
    cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader getHeader();
    /**
     * <pre>
     * 区块头部信息
     * </pre>
     *
     * <code>.BlockHeader header = 1;</code>
     */
    cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeaderOrBuilder getHeaderOrBuilder();

    /**
     * <pre>
     * 区块数据体
     * </pre>
     *
     * <code>.BlockBody body = 2;</code>
     */
    boolean hasBody();
    /**
     * <pre>
     * 区块数据体
     * </pre>
     *
     * <code>.BlockBody body = 2;</code>
     */
    cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody getBody();
    /**
     * <pre>
     * 区块数据体
     * </pre>
     *
     * <code>.BlockBody body = 2;</code>
     */
    cn.aberic.bother.entity.proto.BlockBodyProto.BlockBodyOrBuilder getBodyOrBuilder();
  }
  /**
   * Protobuf type {@code Block}
   */
  public  static final class Block extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:Block)
      BlockOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use Block.newBuilder() to construct.
    private Block(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Block() {
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Block(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
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
              cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.Builder subBuilder = null;
              if (header_ != null) {
                subBuilder = header_.toBuilder();
              }
              header_ = input.readMessage(cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(header_);
                header_ = subBuilder.buildPartial();
              }

              break;
            }
            case 18: {
              cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.Builder subBuilder = null;
              if (body_ != null) {
                subBuilder = body_.toBuilder();
              }
              body_ = input.readMessage(cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(body_);
                body_ = subBuilder.buildPartial();
              }

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
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return cn.aberic.bother.entity.proto.BlockProto.internal_static_Block_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return cn.aberic.bother.entity.proto.BlockProto.internal_static_Block_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              cn.aberic.bother.entity.proto.BlockProto.Block.class, cn.aberic.bother.entity.proto.BlockProto.Block.Builder.class);
    }

    public static final int HEADER_FIELD_NUMBER = 1;
    private cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader header_;
    /**
     * <pre>
     * 区块头部信息
     * </pre>
     *
     * <code>.BlockHeader header = 1;</code>
     */
    public boolean hasHeader() {
      return header_ != null;
    }
    /**
     * <pre>
     * 区块头部信息
     * </pre>
     *
     * <code>.BlockHeader header = 1;</code>
     */
    public cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader getHeader() {
      return header_ == null ? cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.getDefaultInstance() : header_;
    }
    /**
     * <pre>
     * 区块头部信息
     * </pre>
     *
     * <code>.BlockHeader header = 1;</code>
     */
    public cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeaderOrBuilder getHeaderOrBuilder() {
      return getHeader();
    }

    public static final int BODY_FIELD_NUMBER = 2;
    private cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody body_;
    /**
     * <pre>
     * 区块数据体
     * </pre>
     *
     * <code>.BlockBody body = 2;</code>
     */
    public boolean hasBody() {
      return body_ != null;
    }
    /**
     * <pre>
     * 区块数据体
     * </pre>
     *
     * <code>.BlockBody body = 2;</code>
     */
    public cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody getBody() {
      return body_ == null ? cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.getDefaultInstance() : body_;
    }
    /**
     * <pre>
     * 区块数据体
     * </pre>
     *
     * <code>.BlockBody body = 2;</code>
     */
    public cn.aberic.bother.entity.proto.BlockBodyProto.BlockBodyOrBuilder getBodyOrBuilder() {
      return getBody();
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (header_ != null) {
        output.writeMessage(1, getHeader());
      }
      if (body_ != null) {
        output.writeMessage(2, getBody());
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (header_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, getHeader());
      }
      if (body_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, getBody());
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof cn.aberic.bother.entity.proto.BlockProto.Block)) {
        return super.equals(obj);
      }
      cn.aberic.bother.entity.proto.BlockProto.Block other = (cn.aberic.bother.entity.proto.BlockProto.Block) obj;

      boolean result = true;
      result = result && (hasHeader() == other.hasHeader());
      if (hasHeader()) {
        result = result && getHeader()
            .equals(other.getHeader());
      }
      result = result && (hasBody() == other.hasBody());
      if (hasBody()) {
        result = result && getBody()
            .equals(other.getBody());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasHeader()) {
        hash = (37 * hash) + HEADER_FIELD_NUMBER;
        hash = (53 * hash) + getHeader().hashCode();
      }
      if (hasBody()) {
        hash = (37 * hash) + BODY_FIELD_NUMBER;
        hash = (53 * hash) + getBody().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static cn.aberic.bother.entity.proto.BlockProto.Block parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(cn.aberic.bother.entity.proto.BlockProto.Block prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Block}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Block)
        cn.aberic.bother.entity.proto.BlockProto.BlockOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return cn.aberic.bother.entity.proto.BlockProto.internal_static_Block_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return cn.aberic.bother.entity.proto.BlockProto.internal_static_Block_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                cn.aberic.bother.entity.proto.BlockProto.Block.class, cn.aberic.bother.entity.proto.BlockProto.Block.Builder.class);
      }

      // Construct using cn.aberic.bother.entity.proto.BlockProto.Block.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        if (headerBuilder_ == null) {
          header_ = null;
        } else {
          header_ = null;
          headerBuilder_ = null;
        }
        if (bodyBuilder_ == null) {
          body_ = null;
        } else {
          body_ = null;
          bodyBuilder_ = null;
        }
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return cn.aberic.bother.entity.proto.BlockProto.internal_static_Block_descriptor;
      }

      @java.lang.Override
      public cn.aberic.bother.entity.proto.BlockProto.Block getDefaultInstanceForType() {
        return cn.aberic.bother.entity.proto.BlockProto.Block.getDefaultInstance();
      }

      @java.lang.Override
      public cn.aberic.bother.entity.proto.BlockProto.Block build() {
        cn.aberic.bother.entity.proto.BlockProto.Block result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public cn.aberic.bother.entity.proto.BlockProto.Block buildPartial() {
        cn.aberic.bother.entity.proto.BlockProto.Block result = new cn.aberic.bother.entity.proto.BlockProto.Block(this);
        if (headerBuilder_ == null) {
          result.header_ = header_;
        } else {
          result.header_ = headerBuilder_.build();
        }
        if (bodyBuilder_ == null) {
          result.body_ = body_;
        } else {
          result.body_ = bodyBuilder_.build();
        }
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return (Builder) super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof cn.aberic.bother.entity.proto.BlockProto.Block) {
          return mergeFrom((cn.aberic.bother.entity.proto.BlockProto.Block)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(cn.aberic.bother.entity.proto.BlockProto.Block other) {
        if (other == cn.aberic.bother.entity.proto.BlockProto.Block.getDefaultInstance()) return this;
        if (other.hasHeader()) {
          mergeHeader(other.getHeader());
        }
        if (other.hasBody()) {
          mergeBody(other.getBody());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        cn.aberic.bother.entity.proto.BlockProto.Block parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (cn.aberic.bother.entity.proto.BlockProto.Block) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader header_ = null;
      private com.google.protobuf.SingleFieldBuilderV3<
          cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader, cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.Builder, cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeaderOrBuilder> headerBuilder_;
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public boolean hasHeader() {
        return headerBuilder_ != null || header_ != null;
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader getHeader() {
        if (headerBuilder_ == null) {
          return header_ == null ? cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.getDefaultInstance() : header_;
        } else {
          return headerBuilder_.getMessage();
        }
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public Builder setHeader(cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader value) {
        if (headerBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          header_ = value;
          onChanged();
        } else {
          headerBuilder_.setMessage(value);
        }

        return this;
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public Builder setHeader(
          cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.Builder builderForValue) {
        if (headerBuilder_ == null) {
          header_ = builderForValue.build();
          onChanged();
        } else {
          headerBuilder_.setMessage(builderForValue.build());
        }

        return this;
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public Builder mergeHeader(cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader value) {
        if (headerBuilder_ == null) {
          if (header_ != null) {
            header_ =
              cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.newBuilder(header_).mergeFrom(value).buildPartial();
          } else {
            header_ = value;
          }
          onChanged();
        } else {
          headerBuilder_.mergeFrom(value);
        }

        return this;
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public Builder clearHeader() {
        if (headerBuilder_ == null) {
          header_ = null;
          onChanged();
        } else {
          header_ = null;
          headerBuilder_ = null;
        }

        return this;
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.Builder getHeaderBuilder() {
        
        onChanged();
        return getHeaderFieldBuilder().getBuilder();
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      public cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeaderOrBuilder getHeaderOrBuilder() {
        if (headerBuilder_ != null) {
          return headerBuilder_.getMessageOrBuilder();
        } else {
          return header_ == null ?
              cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.getDefaultInstance() : header_;
        }
      }
      /**
       * <pre>
       * 区块头部信息
       * </pre>
       *
       * <code>.BlockHeader header = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader, cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.Builder, cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeaderOrBuilder> 
          getHeaderFieldBuilder() {
        if (headerBuilder_ == null) {
          headerBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader, cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeader.Builder, cn.aberic.bother.entity.proto.BlockHeaderProto.BlockHeaderOrBuilder>(
                  getHeader(),
                  getParentForChildren(),
                  isClean());
          header_ = null;
        }
        return headerBuilder_;
      }

      private cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody body_ = null;
      private com.google.protobuf.SingleFieldBuilderV3<
          cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody, cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.Builder, cn.aberic.bother.entity.proto.BlockBodyProto.BlockBodyOrBuilder> bodyBuilder_;
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public boolean hasBody() {
        return bodyBuilder_ != null || body_ != null;
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody getBody() {
        if (bodyBuilder_ == null) {
          return body_ == null ? cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.getDefaultInstance() : body_;
        } else {
          return bodyBuilder_.getMessage();
        }
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public Builder setBody(cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody value) {
        if (bodyBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          body_ = value;
          onChanged();
        } else {
          bodyBuilder_.setMessage(value);
        }

        return this;
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public Builder setBody(
          cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.Builder builderForValue) {
        if (bodyBuilder_ == null) {
          body_ = builderForValue.build();
          onChanged();
        } else {
          bodyBuilder_.setMessage(builderForValue.build());
        }

        return this;
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public Builder mergeBody(cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody value) {
        if (bodyBuilder_ == null) {
          if (body_ != null) {
            body_ =
              cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.newBuilder(body_).mergeFrom(value).buildPartial();
          } else {
            body_ = value;
          }
          onChanged();
        } else {
          bodyBuilder_.mergeFrom(value);
        }

        return this;
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public Builder clearBody() {
        if (bodyBuilder_ == null) {
          body_ = null;
          onChanged();
        } else {
          body_ = null;
          bodyBuilder_ = null;
        }

        return this;
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.Builder getBodyBuilder() {
        
        onChanged();
        return getBodyFieldBuilder().getBuilder();
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      public cn.aberic.bother.entity.proto.BlockBodyProto.BlockBodyOrBuilder getBodyOrBuilder() {
        if (bodyBuilder_ != null) {
          return bodyBuilder_.getMessageOrBuilder();
        } else {
          return body_ == null ?
              cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.getDefaultInstance() : body_;
        }
      }
      /**
       * <pre>
       * 区块数据体
       * </pre>
       *
       * <code>.BlockBody body = 2;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody, cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.Builder, cn.aberic.bother.entity.proto.BlockBodyProto.BlockBodyOrBuilder> 
          getBodyFieldBuilder() {
        if (bodyBuilder_ == null) {
          bodyBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody, cn.aberic.bother.entity.proto.BlockBodyProto.BlockBody.Builder, cn.aberic.bother.entity.proto.BlockBodyProto.BlockBodyOrBuilder>(
                  getBody(),
                  getParentForChildren(),
                  isClean());
          body_ = null;
        }
        return bodyBuilder_;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:Block)
    }

    // @@protoc_insertion_point(class_scope:Block)
    private static final cn.aberic.bother.entity.proto.BlockProto.Block DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new cn.aberic.bother.entity.proto.BlockProto.Block();
    }

    public static cn.aberic.bother.entity.proto.BlockProto.Block getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Block>
        PARSER = new com.google.protobuf.AbstractParser<Block>() {
      @java.lang.Override
      public Block parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Block(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Block> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Block> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public cn.aberic.bother.entity.proto.BlockProto.Block getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_Block_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_Block_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\013Block.proto\032\021BlockHeader.proto\032\017BlockB" +
      "ody.proto\"?\n\005Block\022\034\n\006header\030\001 \001(\0132\014.Blo" +
      "ckHeader\022\030\n\004body\030\002 \001(\0132\n.BlockBodyB+\n\035cn" +
      ".aberic.bother.entity.protoB\nBlockProtob" +
      "\006proto3"
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
          cn.aberic.bother.entity.proto.BlockHeaderProto.getDescriptor(),
          cn.aberic.bother.entity.proto.BlockBodyProto.getDescriptor(),
        }, assigner);
    internal_static_Block_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_Block_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_Block_descriptor,
        new java.lang.String[] { "Header", "Body", });
    cn.aberic.bother.entity.proto.BlockHeaderProto.getDescriptor();
    cn.aberic.bother.entity.proto.BlockBodyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
