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
// source: node/NodeAssist.proto

package cn.aberic.bother.entity.proto.node;

public final class NodeAssistProto {
  private NodeAssistProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface NodeAssistOrBuilder extends
      // @@protoc_insertion_point(interface_extends:NodeAssist)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    java.util.List<NodeBaseProto.NodeBase>
        getNodeBasesList();
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    NodeBaseProto.NodeBase getNodeBases(int index);
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    int getNodeBasesCount();
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    java.util.List<? extends NodeBaseProto.NodeBaseOrBuilder>
        getNodeBasesOrBuilderList();
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    NodeBaseProto.NodeBaseOrBuilder getNodeBasesOrBuilder(
            int index);
  }
  /**
   * <pre>
   * 当前协助节点对象
   * </pre>
   *
   * Protobuf type {@code NodeAssist}
   */
  public  static final class NodeAssist extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:NodeAssist)
      NodeAssistOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use NodeAssist.newBuilder() to construct.
    private NodeAssist(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private NodeAssist() {
      nodeBases_ = java.util.Collections.emptyList();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private NodeAssist(
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
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                nodeBases_ = new java.util.ArrayList<NodeBaseProto.NodeBase>();
                mutable_bitField0_ |= 0x00000001;
              }
              nodeBases_.add(
                  input.readMessage(NodeBaseProto.NodeBase.parser(), extensionRegistry));
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
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          nodeBases_ = java.util.Collections.unmodifiableList(nodeBases_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return NodeAssistProto.internal_static_NodeAssist_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return NodeAssistProto.internal_static_NodeAssist_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              NodeAssistProto.NodeAssist.class, NodeAssistProto.NodeAssist.Builder.class);
    }

    public static final int NODEBASES_FIELD_NUMBER = 1;
    private java.util.List<NodeBaseProto.NodeBase> nodeBases_;
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    public java.util.List<NodeBaseProto.NodeBase> getNodeBasesList() {
      return nodeBases_;
    }
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    public java.util.List<? extends NodeBaseProto.NodeBaseOrBuilder>
        getNodeBasesOrBuilderList() {
      return nodeBases_;
    }
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    public int getNodeBasesCount() {
      return nodeBases_.size();
    }
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    public NodeBaseProto.NodeBase getNodeBases(int index) {
      return nodeBases_.get(index);
    }
    /**
     * <pre>
     * 当前竞选节点下的节点集合 &lt;= 50
     * </pre>
     *
     * <code>repeated .NodeBase nodeBases = 1;</code>
     */
    public NodeBaseProto.NodeBaseOrBuilder getNodeBasesOrBuilder(
        int index) {
      return nodeBases_.get(index);
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
      for (int i = 0; i < nodeBases_.size(); i++) {
        output.writeMessage(1, nodeBases_.get(i));
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < nodeBases_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, nodeBases_.get(i));
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
      if (!(obj instanceof NodeAssistProto.NodeAssist)) {
        return super.equals(obj);
      }
      NodeAssistProto.NodeAssist other = (NodeAssistProto.NodeAssist) obj;

      boolean result = true;
      result = result && getNodeBasesList()
          .equals(other.getNodeBasesList());
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
      if (getNodeBasesCount() > 0) {
        hash = (37 * hash) + NODEBASES_FIELD_NUMBER;
        hash = (53 * hash) + getNodeBasesList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static NodeAssistProto.NodeAssist parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static NodeAssistProto.NodeAssist parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static NodeAssistProto.NodeAssist parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static NodeAssistProto.NodeAssist parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static NodeAssistProto.NodeAssist parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static NodeAssistProto.NodeAssist parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static NodeAssistProto.NodeAssist parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static NodeAssistProto.NodeAssist parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static NodeAssistProto.NodeAssist parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static NodeAssistProto.NodeAssist parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static NodeAssistProto.NodeAssist parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static NodeAssistProto.NodeAssist parseFrom(
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
    public static Builder newBuilder(NodeAssistProto.NodeAssist prototype) {
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
     * 当前协助节点对象
     * </pre>
     *
     * Protobuf type {@code NodeAssist}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:NodeAssist)
        NodeAssistProto.NodeAssistOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return NodeAssistProto.internal_static_NodeAssist_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return NodeAssistProto.internal_static_NodeAssist_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                NodeAssistProto.NodeAssist.class, NodeAssistProto.NodeAssist.Builder.class);
      }

      // Construct using cn.aberic.bother.entity.proto.node.NodeAssistProto.NodeAssist.newBuilder()
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
          getNodeBasesFieldBuilder();
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        if (nodeBasesBuilder_ == null) {
          nodeBases_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          nodeBasesBuilder_.clear();
        }
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return NodeAssistProto.internal_static_NodeAssist_descriptor;
      }

      @Override
      public NodeAssistProto.NodeAssist getDefaultInstanceForType() {
        return NodeAssistProto.NodeAssist.getDefaultInstance();
      }

      @Override
      public NodeAssistProto.NodeAssist build() {
        NodeAssistProto.NodeAssist result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public NodeAssistProto.NodeAssist buildPartial() {
        NodeAssistProto.NodeAssist result = new NodeAssistProto.NodeAssist(this);
        int from_bitField0_ = bitField0_;
        if (nodeBasesBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            nodeBases_ = java.util.Collections.unmodifiableList(nodeBases_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.nodeBases_ = nodeBases_;
        } else {
          result.nodeBases_ = nodeBasesBuilder_.build();
        }
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
        if (other instanceof NodeAssistProto.NodeAssist) {
          return mergeFrom((NodeAssistProto.NodeAssist)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(NodeAssistProto.NodeAssist other) {
        if (other == NodeAssistProto.NodeAssist.getDefaultInstance()) return this;
        if (nodeBasesBuilder_ == null) {
          if (!other.nodeBases_.isEmpty()) {
            if (nodeBases_.isEmpty()) {
              nodeBases_ = other.nodeBases_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureNodeBasesIsMutable();
              nodeBases_.addAll(other.nodeBases_);
            }
            onChanged();
          }
        } else {
          if (!other.nodeBases_.isEmpty()) {
            if (nodeBasesBuilder_.isEmpty()) {
              nodeBasesBuilder_.dispose();
              nodeBasesBuilder_ = null;
              nodeBases_ = other.nodeBases_;
              bitField0_ = (bitField0_ & ~0x00000001);
              nodeBasesBuilder_ =
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                   getNodeBasesFieldBuilder() : null;
            } else {
              nodeBasesBuilder_.addAllMessages(other.nodeBases_);
            }
          }
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
        NodeAssistProto.NodeAssist parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (NodeAssistProto.NodeAssist) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<NodeBaseProto.NodeBase> nodeBases_ =
        java.util.Collections.emptyList();
      private void ensureNodeBasesIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          nodeBases_ = new java.util.ArrayList<NodeBaseProto.NodeBase>(nodeBases_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilderV3<
          NodeBaseProto.NodeBase, NodeBaseProto.NodeBase.Builder, NodeBaseProto.NodeBaseOrBuilder> nodeBasesBuilder_;

      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public java.util.List<NodeBaseProto.NodeBase> getNodeBasesList() {
        if (nodeBasesBuilder_ == null) {
          return java.util.Collections.unmodifiableList(nodeBases_);
        } else {
          return nodeBasesBuilder_.getMessageList();
        }
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public int getNodeBasesCount() {
        if (nodeBasesBuilder_ == null) {
          return nodeBases_.size();
        } else {
          return nodeBasesBuilder_.getCount();
        }
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public NodeBaseProto.NodeBase getNodeBases(int index) {
        if (nodeBasesBuilder_ == null) {
          return nodeBases_.get(index);
        } else {
          return nodeBasesBuilder_.getMessage(index);
        }
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder setNodeBases(
          int index, NodeBaseProto.NodeBase value) {
        if (nodeBasesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNodeBasesIsMutable();
          nodeBases_.set(index, value);
          onChanged();
        } else {
          nodeBasesBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder setNodeBases(
          int index, NodeBaseProto.NodeBase.Builder builderForValue) {
        if (nodeBasesBuilder_ == null) {
          ensureNodeBasesIsMutable();
          nodeBases_.set(index, builderForValue.build());
          onChanged();
        } else {
          nodeBasesBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder addNodeBases(NodeBaseProto.NodeBase value) {
        if (nodeBasesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNodeBasesIsMutable();
          nodeBases_.add(value);
          onChanged();
        } else {
          nodeBasesBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder addNodeBases(
          int index, NodeBaseProto.NodeBase value) {
        if (nodeBasesBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNodeBasesIsMutable();
          nodeBases_.add(index, value);
          onChanged();
        } else {
          nodeBasesBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder addNodeBases(
          NodeBaseProto.NodeBase.Builder builderForValue) {
        if (nodeBasesBuilder_ == null) {
          ensureNodeBasesIsMutable();
          nodeBases_.add(builderForValue.build());
          onChanged();
        } else {
          nodeBasesBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder addNodeBases(
          int index, NodeBaseProto.NodeBase.Builder builderForValue) {
        if (nodeBasesBuilder_ == null) {
          ensureNodeBasesIsMutable();
          nodeBases_.add(index, builderForValue.build());
          onChanged();
        } else {
          nodeBasesBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder addAllNodeBases(
          Iterable<? extends NodeBaseProto.NodeBase> values) {
        if (nodeBasesBuilder_ == null) {
          ensureNodeBasesIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, nodeBases_);
          onChanged();
        } else {
          nodeBasesBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder clearNodeBases() {
        if (nodeBasesBuilder_ == null) {
          nodeBases_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          nodeBasesBuilder_.clear();
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public Builder removeNodeBases(int index) {
        if (nodeBasesBuilder_ == null) {
          ensureNodeBasesIsMutable();
          nodeBases_.remove(index);
          onChanged();
        } else {
          nodeBasesBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public NodeBaseProto.NodeBase.Builder getNodeBasesBuilder(
          int index) {
        return getNodeBasesFieldBuilder().getBuilder(index);
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public NodeBaseProto.NodeBaseOrBuilder getNodeBasesOrBuilder(
          int index) {
        if (nodeBasesBuilder_ == null) {
          return nodeBases_.get(index);  } else {
          return nodeBasesBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public java.util.List<? extends NodeBaseProto.NodeBaseOrBuilder>
           getNodeBasesOrBuilderList() {
        if (nodeBasesBuilder_ != null) {
          return nodeBasesBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(nodeBases_);
        }
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public NodeBaseProto.NodeBase.Builder addNodeBasesBuilder() {
        return getNodeBasesFieldBuilder().addBuilder(
            NodeBaseProto.NodeBase.getDefaultInstance());
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public NodeBaseProto.NodeBase.Builder addNodeBasesBuilder(
          int index) {
        return getNodeBasesFieldBuilder().addBuilder(
            index, NodeBaseProto.NodeBase.getDefaultInstance());
      }
      /**
       * <pre>
       * 当前竞选节点下的节点集合 &lt;= 50
       * </pre>
       *
       * <code>repeated .NodeBase nodeBases = 1;</code>
       */
      public java.util.List<NodeBaseProto.NodeBase.Builder>
           getNodeBasesBuilderList() {
        return getNodeBasesFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilderV3<
          NodeBaseProto.NodeBase, NodeBaseProto.NodeBase.Builder, NodeBaseProto.NodeBaseOrBuilder>
          getNodeBasesFieldBuilder() {
        if (nodeBasesBuilder_ == null) {
          nodeBasesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
              NodeBaseProto.NodeBase, NodeBaseProto.NodeBase.Builder, NodeBaseProto.NodeBaseOrBuilder>(
                  nodeBases_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          nodeBases_ = null;
        }
        return nodeBasesBuilder_;
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


      // @@protoc_insertion_point(builder_scope:NodeAssist)
    }

    // @@protoc_insertion_point(class_scope:NodeAssist)
    private static final NodeAssistProto.NodeAssist DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new NodeAssistProto.NodeAssist();
    }

    public static NodeAssistProto.NodeAssist getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<NodeAssist>
        PARSER = new com.google.protobuf.AbstractParser<NodeAssist>() {
      @Override
      public NodeAssist parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new NodeAssist(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<NodeAssist> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<NodeAssist> getParserForType() {
      return PARSER;
    }

    @Override
    public NodeAssistProto.NodeAssist getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_NodeAssist_descriptor;
  private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_NodeAssist_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\025node/NodeAssist.proto\032\023node/NodeBase.p" +
      "roto\"*\n\nNodeAssist\022\034\n\tnodeBases\030\001 \003(\0132\t." +
      "NodeBaseB5\n\"cn.aberic.bother.entity.prot" +
      "o.nodeB\017NodeAssistProtob\006proto3"
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
          NodeBaseProto.getDescriptor(),
        }, assigner);
    internal_static_NodeAssist_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_NodeAssist_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_NodeAssist_descriptor,
        new String[] { "NodeBases", });
    NodeBaseProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}