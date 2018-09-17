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
// source: consensus/ConnectSelf.proto

package cn.aberic.bother.entity.proto.consensus;

public final class ConnectSelfProto {
  private ConnectSelfProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface ConnectSelfOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ConnectSelf)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <pre>
     * 自身节点等级，等级0表示未成为任何小组Leader，1表示是一个小组Leader，2表示51个小组Leader，以此类推
     * </pre>
     *
     * <code>int32 level = 1;</code>
     */
    int getLevel();

    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    java.util.List<GroupInfoProto.GroupInfo>
        getGroupsList();
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    GroupInfoProto.GroupInfo getGroups(int index);
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    int getGroupsCount();
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    java.util.List<? extends GroupInfoProto.GroupInfoOrBuilder>
        getGroupsOrBuilderList();
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    GroupInfoProto.GroupInfoOrBuilder getGroupsOrBuilder(
            int index);
  }
  /**
   * <pre>
   * 连接信息对象，有且仅有一个实例
   * </pre>
   *
   * Protobuf type {@code ConnectSelf}
   */
  public  static final class ConnectSelf extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ConnectSelf)
      ConnectSelfOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use ConnectSelf.newBuilder() to construct.
    private ConnectSelf(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private ConnectSelf() {
      level_ = 0;
      groups_ = java.util.Collections.emptyList();
    }

    @Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private ConnectSelf(
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
            case 8: {

              level_ = input.readInt32();
              break;
            }
            case 18: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                groups_ = new java.util.ArrayList<GroupInfoProto.GroupInfo>();
                mutable_bitField0_ |= 0x00000002;
              }
              groups_.add(
                  input.readMessage(GroupInfoProto.GroupInfo.parser(), extensionRegistry));
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
          groups_ = java.util.Collections.unmodifiableList(groups_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ConnectSelfProto.internal_static_ConnectSelf_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ConnectSelfProto.internal_static_ConnectSelf_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ConnectSelfProto.ConnectSelf.class, ConnectSelfProto.ConnectSelf.Builder.class);
    }

    private int bitField0_;
    public static final int LEVEL_FIELD_NUMBER = 1;
    private int level_;
    /**
     * <pre>
     * 自身节点等级，等级0表示未成为任何小组Leader，1表示是一个小组Leader，2表示51个小组Leader，以此类推
     * </pre>
     *
     * <code>int32 level = 1;</code>
     */
    public int getLevel() {
      return level_;
    }

    public static final int GROUPS_FIELD_NUMBER = 2;
    private java.util.List<GroupInfoProto.GroupInfo> groups_;
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    public java.util.List<GroupInfoProto.GroupInfo> getGroupsList() {
      return groups_;
    }
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    public java.util.List<? extends GroupInfoProto.GroupInfoOrBuilder>
        getGroupsOrBuilderList() {
      return groups_;
    }
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    public int getGroupsCount() {
      return groups_.size();
    }
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    public GroupInfoProto.GroupInfo getGroups(int index) {
      return groups_.get(index);
    }
    /**
     * <pre>
     * 当前连接小组集合
     * </pre>
     *
     * <code>repeated .GroupInfo groups = 2;</code>
     */
    public GroupInfoProto.GroupInfoOrBuilder getGroupsOrBuilder(
        int index) {
      return groups_.get(index);
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
      if (level_ != 0) {
        output.writeInt32(1, level_);
      }
      for (int i = 0; i < groups_.size(); i++) {
        output.writeMessage(2, groups_.get(i));
      }
      unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (level_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, level_);
      }
      for (int i = 0; i < groups_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, groups_.get(i));
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
      if (!(obj instanceof ConnectSelfProto.ConnectSelf)) {
        return super.equals(obj);
      }
      ConnectSelfProto.ConnectSelf other = (ConnectSelfProto.ConnectSelf) obj;

      boolean result = true;
      result = result && (getLevel()
          == other.getLevel());
      result = result && getGroupsList()
          .equals(other.getGroupsList());
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
      hash = (37 * hash) + LEVEL_FIELD_NUMBER;
      hash = (53 * hash) + getLevel();
      if (getGroupsCount() > 0) {
        hash = (37 * hash) + GROUPS_FIELD_NUMBER;
        hash = (53 * hash) + getGroupsList().hashCode();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ConnectSelfProto.ConnectSelf parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ConnectSelfProto.ConnectSelf parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ConnectSelfProto.ConnectSelf parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ConnectSelfProto.ConnectSelf parseFrom(
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
    public static Builder newBuilder(ConnectSelfProto.ConnectSelf prototype) {
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
     * 连接信息对象，有且仅有一个实例
     * </pre>
     *
     * Protobuf type {@code ConnectSelf}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ConnectSelf)
        ConnectSelfProto.ConnectSelfOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ConnectSelfProto.internal_static_ConnectSelf_descriptor;
      }

      @Override
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ConnectSelfProto.internal_static_ConnectSelf_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ConnectSelfProto.ConnectSelf.class, ConnectSelfProto.ConnectSelf.Builder.class);
      }

      // Construct using cn.aberic.bother.entity.proto.consensus.ConnectSelfProto.ConnectSelf.newBuilder()
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
          getGroupsFieldBuilder();
        }
      }
      @Override
      public Builder clear() {
        super.clear();
        level_ = 0;

        if (groupsBuilder_ == null) {
          groups_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          groupsBuilder_.clear();
        }
        return this;
      }

      @Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ConnectSelfProto.internal_static_ConnectSelf_descriptor;
      }

      @Override
      public ConnectSelfProto.ConnectSelf getDefaultInstanceForType() {
        return ConnectSelfProto.ConnectSelf.getDefaultInstance();
      }

      @Override
      public ConnectSelfProto.ConnectSelf build() {
        ConnectSelfProto.ConnectSelf result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @Override
      public ConnectSelfProto.ConnectSelf buildPartial() {
        ConnectSelfProto.ConnectSelf result = new ConnectSelfProto.ConnectSelf(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        result.level_ = level_;
        if (groupsBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            groups_ = java.util.Collections.unmodifiableList(groups_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.groups_ = groups_;
        } else {
          result.groups_ = groupsBuilder_.build();
        }
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
        if (other instanceof ConnectSelfProto.ConnectSelf) {
          return mergeFrom((ConnectSelfProto.ConnectSelf)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ConnectSelfProto.ConnectSelf other) {
        if (other == ConnectSelfProto.ConnectSelf.getDefaultInstance()) return this;
        if (other.getLevel() != 0) {
          setLevel(other.getLevel());
        }
        if (groupsBuilder_ == null) {
          if (!other.groups_.isEmpty()) {
            if (groups_.isEmpty()) {
              groups_ = other.groups_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureGroupsIsMutable();
              groups_.addAll(other.groups_);
            }
            onChanged();
          }
        } else {
          if (!other.groups_.isEmpty()) {
            if (groupsBuilder_.isEmpty()) {
              groupsBuilder_.dispose();
              groupsBuilder_ = null;
              groups_ = other.groups_;
              bitField0_ = (bitField0_ & ~0x00000002);
              groupsBuilder_ =
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                   getGroupsFieldBuilder() : null;
            } else {
              groupsBuilder_.addAllMessages(other.groups_);
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
        ConnectSelfProto.ConnectSelf parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ConnectSelfProto.ConnectSelf) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int level_ ;
      /**
       * <pre>
       * 自身节点等级，等级0表示未成为任何小组Leader，1表示是一个小组Leader，2表示51个小组Leader，以此类推
       * </pre>
       *
       * <code>int32 level = 1;</code>
       */
      public int getLevel() {
        return level_;
      }
      /**
       * <pre>
       * 自身节点等级，等级0表示未成为任何小组Leader，1表示是一个小组Leader，2表示51个小组Leader，以此类推
       * </pre>
       *
       * <code>int32 level = 1;</code>
       */
      public Builder setLevel(int value) {

        level_ = value;
        onChanged();
        return this;
      }
      /**
       * <pre>
       * 自身节点等级，等级0表示未成为任何小组Leader，1表示是一个小组Leader，2表示51个小组Leader，以此类推
       * </pre>
       *
       * <code>int32 level = 1;</code>
       */
      public Builder clearLevel() {

        level_ = 0;
        onChanged();
        return this;
      }

      private java.util.List<GroupInfoProto.GroupInfo> groups_ =
        java.util.Collections.emptyList();
      private void ensureGroupsIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          groups_ = new java.util.ArrayList<GroupInfoProto.GroupInfo>(groups_);
          bitField0_ |= 0x00000002;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilderV3<
          GroupInfoProto.GroupInfo, GroupInfoProto.GroupInfo.Builder, GroupInfoProto.GroupInfoOrBuilder> groupsBuilder_;

      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public java.util.List<GroupInfoProto.GroupInfo> getGroupsList() {
        if (groupsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(groups_);
        } else {
          return groupsBuilder_.getMessageList();
        }
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public int getGroupsCount() {
        if (groupsBuilder_ == null) {
          return groups_.size();
        } else {
          return groupsBuilder_.getCount();
        }
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public GroupInfoProto.GroupInfo getGroups(int index) {
        if (groupsBuilder_ == null) {
          return groups_.get(index);
        } else {
          return groupsBuilder_.getMessage(index);
        }
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder setGroups(
          int index, GroupInfoProto.GroupInfo value) {
        if (groupsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureGroupsIsMutable();
          groups_.set(index, value);
          onChanged();
        } else {
          groupsBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder setGroups(
          int index, GroupInfoProto.GroupInfo.Builder builderForValue) {
        if (groupsBuilder_ == null) {
          ensureGroupsIsMutable();
          groups_.set(index, builderForValue.build());
          onChanged();
        } else {
          groupsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder addGroups(GroupInfoProto.GroupInfo value) {
        if (groupsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureGroupsIsMutable();
          groups_.add(value);
          onChanged();
        } else {
          groupsBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder addGroups(
          int index, GroupInfoProto.GroupInfo value) {
        if (groupsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureGroupsIsMutable();
          groups_.add(index, value);
          onChanged();
        } else {
          groupsBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder addGroups(
          GroupInfoProto.GroupInfo.Builder builderForValue) {
        if (groupsBuilder_ == null) {
          ensureGroupsIsMutable();
          groups_.add(builderForValue.build());
          onChanged();
        } else {
          groupsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder addGroups(
          int index, GroupInfoProto.GroupInfo.Builder builderForValue) {
        if (groupsBuilder_ == null) {
          ensureGroupsIsMutable();
          groups_.add(index, builderForValue.build());
          onChanged();
        } else {
          groupsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder addAllGroups(
          Iterable<? extends GroupInfoProto.GroupInfo> values) {
        if (groupsBuilder_ == null) {
          ensureGroupsIsMutable();
          com.google.protobuf.AbstractMessageLite.Builder.addAll(
              values, groups_);
          onChanged();
        } else {
          groupsBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder clearGroups() {
        if (groupsBuilder_ == null) {
          groups_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          groupsBuilder_.clear();
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public Builder removeGroups(int index) {
        if (groupsBuilder_ == null) {
          ensureGroupsIsMutable();
          groups_.remove(index);
          onChanged();
        } else {
          groupsBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public GroupInfoProto.GroupInfo.Builder getGroupsBuilder(
          int index) {
        return getGroupsFieldBuilder().getBuilder(index);
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public GroupInfoProto.GroupInfoOrBuilder getGroupsOrBuilder(
          int index) {
        if (groupsBuilder_ == null) {
          return groups_.get(index);  } else {
          return groupsBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public java.util.List<? extends GroupInfoProto.GroupInfoOrBuilder>
           getGroupsOrBuilderList() {
        if (groupsBuilder_ != null) {
          return groupsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(groups_);
        }
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public GroupInfoProto.GroupInfo.Builder addGroupsBuilder() {
        return getGroupsFieldBuilder().addBuilder(
            GroupInfoProto.GroupInfo.getDefaultInstance());
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public GroupInfoProto.GroupInfo.Builder addGroupsBuilder(
          int index) {
        return getGroupsFieldBuilder().addBuilder(
            index, GroupInfoProto.GroupInfo.getDefaultInstance());
      }
      /**
       * <pre>
       * 当前连接小组集合
       * </pre>
       *
       * <code>repeated .GroupInfo groups = 2;</code>
       */
      public java.util.List<GroupInfoProto.GroupInfo.Builder>
           getGroupsBuilderList() {
        return getGroupsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilderV3<
          GroupInfoProto.GroupInfo, GroupInfoProto.GroupInfo.Builder, GroupInfoProto.GroupInfoOrBuilder>
          getGroupsFieldBuilder() {
        if (groupsBuilder_ == null) {
          groupsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
              GroupInfoProto.GroupInfo, GroupInfoProto.GroupInfo.Builder, GroupInfoProto.GroupInfoOrBuilder>(
                  groups_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          groups_ = null;
        }
        return groupsBuilder_;
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


      // @@protoc_insertion_point(builder_scope:ConnectSelf)
    }

    // @@protoc_insertion_point(class_scope:ConnectSelf)
    private static final ConnectSelfProto.ConnectSelf DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ConnectSelfProto.ConnectSelf();
    }

    public static ConnectSelfProto.ConnectSelf getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<ConnectSelf>
        PARSER = new com.google.protobuf.AbstractParser<ConnectSelf>() {
      @Override
      public ConnectSelf parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ConnectSelf(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<ConnectSelf> parser() {
      return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<ConnectSelf> getParserForType() {
      return PARSER;
    }

    @Override
    public ConnectSelfProto.ConnectSelf getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ConnectSelf_descriptor;
  private static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ConnectSelf_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\033consensus/ConnectSelf.proto\032\031consensus" +
      "/GroupInfo.proto\"8\n\013ConnectSelf\022\r\n\005level" +
      "\030\001 \001(\005\022\032\n\006groups\030\002 \003(\0132\n.GroupInfoB;\n\'cn" +
      ".aberic.bother.entity.proto.consensusB\020C" +
      "onnectSelfProtob\006proto3"
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
          GroupInfoProto.getDescriptor(),
        }, assigner);
    internal_static_ConnectSelf_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ConnectSelf_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ConnectSelf_descriptor,
        new String[] { "Level", "Groups", });
    GroupInfoProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}