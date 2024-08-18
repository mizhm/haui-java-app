use master
go

create database coffee_shop
go

use coffee_shop
go

--user section
create table [user]
(
    id         int identity,
    [name]     nvarchar(100)       not null,
    email      varchar(100) unique not null,
    [password] varchar(100)        not null,
    [role]     tinyint default (0),
    primary key (id)
)
go

create proc usp_insert_user(
    @_name nvarchar(100),
    @_email varchar(100),
    @_password varchar(100),
    @_role tinyint = 0,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(200) = '' output
)
as
begin try
    if exists (select email from [user] where email = @_email)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Email đã tồn tại, vui lòng nhập lại';
        end;
    else
        begin
            begin tran;
            insert into [user]
            ([name],
             email,
             [password],
             [role])
            values (@_name, @_email, @_password, @_role);

            set @_out_stt = 1;
            set @_out_msg = N'Thêm người dùng thành công';

            If @@trancount > 0
                commit tran;
        end;
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Thêm không thành công: ' + ERROR_MESSAGE();

    if @@trancount > 0
        rollback tran;
end catch;

go

-- Them user admin
exec usp_insert_user @_name = N'Quản trị viên',
     @_email = 'admin@gmail.com',
     @_password = '123456',
     @_role = 1;

go

exec usp_insert_user @_name = N'Nhân viên',
     @_email = 'user@gmail.com',
     @_password = '123456';

go

create proc usp_update_user(
    @_id int,
    @_name nvarchar(100),
    @_email varchar(100),
    @_password varchar(100),
    @_role tinyint = 0,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(200) = '' output
)
as
begin try
    if not exists (select id from [user] where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Người dùng không tồn tại, vui lòng nhập lại';
        end;
    else
        if exists (select email from [user] where email = @_email and id != @_id)
            begin
                set @_out_stt = 0;
                set @_out_msg = N'Email đã tồn tại, vui lòng nhập lại';
            end;
        else
            begin
                begin tran;
                update [user]
                set [name]     = @_name,
                    email      = @_email,
                    [password] = @_password,
                    [role]     = @_role
                where id = @_id;

                set @_out_stt = 1;
                set @_out_msg = N'Cập nhật người dùng thành công';

                if @@trancount > 0
                    commit tran;
            end;
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Cập nhật không thành công: ' + ERROR_MESSAGE();

    if @@trancount > 0
        rollback tran;
end catch;

go

create proc usp_check_user(
    @_email varchar(100),
    @_password varchar(100)
)
as
begin
    select top 1 *
    from [user]
    where email = @_email
      and [password] = @_password;
end;

go

exec usp_check_user 'admin@gmail.com', '123456';

go

create proc usp_get_all_user(
    @_name nvarchar(100) = NULL,
    @_email varchar(100) = NULL,
    @_role tinyint = NULL
)
as
declare @sql nvarchar(max) = N'select * from [user] where 1=1';

    if (@_name is not null)
        set @sql = concat(@sql, N' and name like ''%', @_name, N'%''');

    if (@_email is not null)
        set @sql = concat(@sql, ' and [email]=''', @_email, '''');

    if (@_role is not null)
        set @sql = concat(@sql, N' and role=', @_role);

    exec (@sql);

go

exec usp_get_all_user @_email = N'admin@gmail.com'
go

create proc usp_get_user_by_id(@_id int)
as
select *
from [user]
where id = @_id;

go

--exec sp_getuserbyid 1;

go

create proc usp_delete_user(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(200) = '' output
)
as
begin try
    if exists
        (select u.id
         from [user] u
                  join bill b
                       on b.[user_id] = u.id
         where u.id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Người dùng đang có liên kết hoá đơn, không thể xoá';
        end;
    else
        begin
            begin tran;
            delete [user]
            where id = @_id;

            set @_out_stt = 1;
            set @_out_msg = N'Xoá thành công';
            if @@trancount > 0
                commit tran;
        end;
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Xoá không thành công: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch;

go

-- category section
create table category
(
    id         int identity,
    name       nvarchar(255) unique not null,
    status     bit default (1),
    created_at datetime             not null,
    updated_at datetime,
    primary key (id)
)
go

create proc usp_insert_category(
    @_name nvarchar(255),
    @_status bit = 1,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if exists(select name from category where name = @_name)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Danh mục đã tồn tại';
        end
    else
        begin
            begin tran;
            insert into category(name, status, created_at, updated_at)
            values (@_name, @_status, getdate(), getdate());
            set @_out_stt = 1;
            set @_out_msg = N'Thêm danh mục thành công';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Thêm danh mục thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

-- Thêm danh mục
exec usp_insert_category @_name = N'Cà Phê Việt';
go

exec usp_insert_category @_name = N'Cà Phê Tây';
go

exec usp_insert_category @_name = N'Sinh Tố';
go

exec usp_insert_category @_name = N'Trà';
go

exec usp_insert_category @_name = N'Trái Cây';
go

exec usp_insert_category @_name = N'Sữa Chua';
go

exec usp_insert_category @_name = N'Khác';
go

create proc usp_update_category(
    @_id int,
    @_name nvarchar(255),
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if not exists(select * from category where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Danh mục không tồn tại';
        end
    else
        begin
            if exists(select * from category where name = @_name and id <> @_id)
                begin
                    set @_out_stt = 0;
                    set @_out_msg = N'Tên danh mục đã được sử dụng';
                end
            else
                begin
                    begin tran;
                    update category
                    set name= @_name,
                        status = @_status,
                        updated_at = getdate()
                    where id = @_id;
                    set @_out_stt = 1;
                    set @_out_msg = N'Sửa danh mục thành công';
                    if @@trancount > 0
                        commit tran;
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Sửa danh mục thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_delete_category(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(max) = '' output
)
as
begin try
    if exists (select *
               from category
                        join product on product.category_id = category.id
               where category.id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Danh mục này đang có sản phẩm, không thể xóa'
        end
    else
        begin
            begin tran;
            delete category
            where id = @_id;
            set @_out_stt = 1;
            set @_out_msg = N'Xóa danh mục thành công';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Xóa danh mục thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go


create proc usp_get_all_category(
    @_name nvarchar(255) = null,
    @_status bit = null
)
as
begin
    declare @sql nvarchar(max) = N'select * from category where 1 = 1';
    if (@_name is not null)
        set @sql = concat(@sql, N' and name like ''%', @_name, N'%''');
    if (@_status is not null)
        set @sql = concat(@sql, N' and status = ', @_status);
    exec (@sql)
end
go



create proc usp_get_category_by_id(
    @_id int
) as
begin
    select top 1 * from category where id = @_id
end
go

-- product section
create table product
(
    id          int identity,
    name        nvarchar(255) unique not null,
    price       float                not null,
    status      bit default (1),
    created_at  datetime             not null,
    updated_at  datetime             not null,
    category_id int                  not null,
    primary key (id),
    foreign key (category_id) references category (id)
)
go

create proc usp_insert_product(
    @_category_id INT,
    @_name NVARCHAR(100),
    @_price FLOAT,
    @_status BIT = 1,
    @_out_stt BIT = 1 OUTPUT,
    @_out_msg NVARCHAR(200) = '' OUTPUT
) as
begin try
    if not exists(select * from category where id = @_category_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Danh mục không tồn tại';
        end
    else
        begin
            if exists(select * from product where lower(name) = lower(@_name))
                begin
                    set @_out_stt = 0;
                    set @_out_msg = N'Tên sản phẩm đã được sử dụng';
                end
            else
                begin
                    begin tran;
                    insert into product(name, status, price, category_id,
                                        created_at, updated_at)
                    values (@_name, @_status, @_price, @_category_id, getdate(),
                            getdate());
                    set @_out_stt = 1;
                    set @_out_msg = N'Thêm sản phẩm thành công'
                    if @@trancount > 0
                        commit tran
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Thêm sản phẩm thất bại: ' + error_message()
    if @@trancount > 0
        rollback tran
end catch
go
EXEC usp_insert_product 1, N'Bạc xỉu', 35000;

GO

EXEC usp_insert_product 1, N'Cà phê sữa tươi', 35000;

GO

EXEC usp_insert_product 1, N'Cà phê đen', 29000;

GO

EXEC usp_insert_product 1, N'Cà phê nâu', 35000;

GO

EXEC usp_insert_product 1, N'Cà phê sữa tươi', 35000;

GO

EXEC usp_insert_product 2, N'Latte', 45000;

GO

EXEC usp_insert_product 2, N'Cappuchino', 45000;

GO

EXEC usp_insert_product 2, N'Espresso', 30000;

GO

EXEC usp_insert_product 3, N'Sinh tố bơ', 59000;

GO

EXEC usp_insert_product 3, N'Sinh tố xoài', 5000;

GO

EXEC usp_insert_product 4, N'Trà cam quế', 45000;

GO

EXEC usp_insert_product 4, N'Trà đào chanh leo', 45000;

GO

EXEC usp_insert_product 4, N'Trà quất mật ong', 45000;

GO

EXEC usp_insert_product 4, N'Trà lip ton', 25000;

GO

EXEC usp_insert_product 4, N'Trà mạn', 35000;

GO

EXEC usp_insert_product 5, N'Cóc xanh (theo mùa)', 55000;

GO

EXEC usp_insert_product 5, N'Chanh tươi', 39000;

GO

EXEC usp_insert_product 5, N'Dưa hấu', 49000;

GO

EXEC usp_insert_product 5, N'Chanh leo', 49000;

GO

EXEC usp_insert_product 5, N'Cam tươi', 65000;

GO

EXEC usp_insert_product 5, N'Ổi', 45000;

GO

EXEC usp_insert_product 5, N'Xoài xanh', 45000;

GO

EXEC usp_insert_product 6, N'Sữa chua dầm đá', 35000;

GO

EXEC usp_insert_product 6, N'Sữa chua ca cao', 40000;

GO

EXEC usp_insert_product 6, N'Sữa chua cà phê', 40000;

GO

EXEC usp_insert_product 6, N'Sữa chua trái cây', 55000;

GO

EXEC usp_insert_product 7, N'Hạt hướng dương', 25000;

GO

EXEC usp_insert_product 7, N'Lạc rang', 25000;

GO

EXEC usp_insert_product 7, N'Ngô cay', 25000;

GO

EXEC usp_insert_product 7, N'Bánh đậu xanh & Kẹo lạc', 25000;

GO

EXEC usp_insert_product 7, N'Bánh sừng bò chấm sữa', 25000;

GO

EXEC usp_insert_product 7, N'Thịt bò khô', 40000;

GO

create proc usp_update_product(
    @_id int,
    @_category_id INT,
    @_name NVARCHAR(100),
    @_price FLOAT,
    @_status BIT = 1,
    @_out_stt BIT = 1 OUTPUT,
    @_out_msg NVARCHAR(200) = '' OUTPUT
) as
begin try
    if not exists(select * from product where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Sản phẩm không tồn tại';
        end
    else
        begin
            if exists(select * from product where name = @_name and id <> @_id)
                begin
                    set @_out_stt = 0;
                    set @_out_msg = N'Tên sản phẩm đã được sử dụng';
                end
            else
                begin
                    begin tran;
                    update product
                    set name= @_name,
                        status = @_status,
                        price = @_price,
                        category_id = @_category_id,
                        updated_at= getdate()
                    where id = @_id;
                    set @_out_stt = 1;
                    set @_out_msg = N'Cập nhật sản phẩm thành công'
                    if @@trancount > 0
                        commit tran
                end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Cập nhật sản phẩm thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_delete_product(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
) as
begin try
    if exists(select *
              from product
                       join bill_detail on bill_detail.product_id = product.id
              where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Sản phẩm có trong hóa đơn, không thể xóa';
        end
    else
        begin
            begin tran;
            delete product
            where id = @_id;
            set @_out_stt = 1;
            set @_out_msg = N'Xóa sản phẩm thành công';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Xóa sản phẩm thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_get_all_product(
    @_name nvarchar(100) = null,
    @_category_id int = null,
    @_fromPrice float = null,
    @_toPrice float = null,
    @_status bit = null
)
as
declare
    @sql NVARCHAR(MAX)
        = N'
		select p.*, c.[name] category_name
		from product p
		join category c
        on c.id = p.category_id where 1=1 and c.status = 1';

    if (@_name is not null)
        set @sql = concat(@sql, N' and p.name like ''%', @_name, N'%''');

    if (@_category_id is not null)
        set @sql = concat(@sql, N' and p.category_id = ', @_category_id);

    if (@_fromPrice is not null)
        set @sql = concat(@sql, N' and p.price >= ', @_fromPrice);

    if (@_toPrice is not null)
        set @sql = concat(@sql, N' and p.price <= ', @_toPrice);

    if (@_status is not null)
        set @sql = concat(@sql, N' and p.status = ', @_status);

    exec (@sql);

go

exec usp_get_all_product
go

create proc usp_get_product_by_id(
    @_id int
) as
begin
    select top 1 * from product where id = @_id
end
go

-- bill section
create table bill
(
    id         int identity,
    status     bit default (1),
    created_at datetime not null,
    updated_at datetime,
    user_id    int,
    primary key (id),
    foreign key (user_id) references [user] (id)
)
go

create table bill_detail
(
    bill_id    int   not null,
    product_id int   not null,
    amount     int   not null,
    price      float not null,
    primary key (bill_id, product_id),
    foreign key (bill_id) references bill (id),
    foreign key (product_id) references product (id)
)
go

create proc usp_insert_bill(
    @_status bit,
    @_user_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar = '' output
)
as
begin try
    begin tran;
    insert into bill(status, user_id, created_at, updated_at)
    values (@_status, @_user_id, getdate(), getdate());
    set @_out_stt = 1;
    set @_out_msg = N'Tạo hóa đơn thành công';
    if @@trancount > 0
        commit tran
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = error_message();
    if @@trancount > 0
        rollback tran
end catch
go

create proc usp_update_bill(
    @_id int,
    @_status bit,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(255) = '' output
)
as
begin try
    if not exists(select * from bill where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Hóa đơn không tồn tại';
        end
    else
        begin
            begin
                begin tran;
                update bill
                set status     = @_status,
                    updated_at = getdate()
                where id = @_id;

                set @_out_stt = 1;
                set @_out_msg = N'Cập nhật hóa đơn thành công';
                if @@trancount > 0
                    commit tran;
            end
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Cập nhật hóa đơn thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_delete_bill(
    @_id int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(max) = '' output
)
as
begin try
    if not exists(select id from bill where id = @_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Hóa đơn không tồn tại';
        end
    else
        begin
            begin tran;
            if exists(select bill_id from bill_detail where bill_id = @_id)
                delete
                from bill_detail
                where bill_id = @_id;
            delete
            from bill
            where id = @_id;

            set @_out_stt = 1;
            set @_out_msg = N'Xóa hóa đơn thành công';
            if @@trancount > 0
                commit tran;
        end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Xóa hóa đơn thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_get_all_bill(
    @_id int = null,
    @_status bit = null,
    @_date date = null
)
as
declare
    @sql nvarchar(max) = N'select b.*, sum(bd.price * bd.amount) [total], u.name' +
                         N' from bill b' +
                         N' join [user] u on b.user_id = u.id' +
                         N' left join bill_detail bd on b.id = bd.bill_id' +
                         N' where 1 = 1';
    if (@_id is not null)
        set @sql = concat(@sql, N' and b.id = ', @_id);
    if (@_status is not null)
        set @sql = concat(@sql, N' and status = ', @_status);
    if (@_date is not null)
        set @sql = concat(@sql, N' and created_at >= ''', @_date, N''' and created_at < dateadd(day, 1, ''', @_date,
                          ''')');
    set @sql = concat(@sql,
                      N' group by b.id, b.status, b.created_at, b.updated_at, b.user_id, u.name')
    exec (@sql);
go

exec usp_get_all_bill
go

create proc usp_get_new_bill
as
select top 1 *
from bill
order by created_at desc
go
-- select b.*, sum(bd.price * bd.amount) [total]
-- from bill b
--          join bill_detail bd on b.id = bd.bill_id
-- group by b.id, b.status, b.created_at, b.updated_at
-- go

create proc usp_insert_bill_detail(
    @_bill_id int,
    @_product_id int,
    @_amount int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar = '' output
)
as
begin try
    if not exists(select * from bill where id = @_bill_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Hóa đơn không tồn tại';
        end
    else
        if not exists(select * from product where id = @_product_id)
            begin
                set @_out_stt = 0;
                set @_out_msg = N'Sản phẩm không tồn tại';
            end
        else
            if @_amount < 1
                begin
                    set @_out_stt = 0;
                    set @_out_msg = N'Số lượng sản phẩm phải lớn hơn 0';
                end
            else
                if exists(select *
                          from bill_detail
                          where bill_id = @_bill_id
                            and product_id = @_product_id)
                    begin
                        begin tran;
                        update bill_detail
                        set amount += @_amount
                        where bill_id = @_bill_id
                          and product_id = @_product_id;

                        update bill
                        set updated_at = getdate()
                        where id = @_bill_id;

                        set @_out_stt = 1;
                        set @_out_msg = N'Cập nhật chi tiết hóa đơn thành công';
                        if @@TRANCOUNT > 0
                            commit tran;
                    end
                else
                    begin
                        begin tran;
                        declare @_price float = (select price
                                                 from product
                                                 where id = @_product_id);
                        insert into bill_detail(bill_id, product_id, amount, price)
                        values (@_bill_id, @_product_id, @_amount, @_price);

                        update bill
                        set updated_at = getdate()
                        where id = @_bill_id;

                        set @_out_stt = 1;
                        set @_out_msg = N'Thêm chi tiết hóa đơn thành công';
                        if @@trancount > 0
                            commit tran;
                    end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Thêm chi tiết hóa đơn thất bại: ' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_update_bill_detail(
    @_bill_id int,
    @_product_id int,
    @_amount int,
    @_out_stt bit = 1 output,
    @_out_msg nvarchar(max) = N'' output
)
as
begin try
    if not exists(select id from bill where id = @_bill_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Hóa đơn không tồn tại';
        end
    else
        if not exists(select id from product where id = @_product_id)
            begin
                set @_out_stt = 0;
                set @_out_msg = N'Sản phẩm không tồn tại';
            end
        else
            if @_amount < 1
                begin
                    set @_out_stt = 0;
                    set @_out_msg = N'Số lượng sản phẩm phải lớn hơn 0';
                end
            else
                if not exists(select *
                              from bill_detail
                              where bill_id = @_bill_id
                                and product_id = @_product_id)
                    begin
                        set @_out_stt = 0;
                        set @_out_msg = N'Chi tiết hóa đơn không tồn tại';
                    end
                else
                    begin
                        begin tran;
                        update bill_detail
                        set amount = @_amount
                        where bill_id = @_bill_id
                          and product_id = @_product_id

                        update bill
                        set updated_at = getdate()
                        where id = @_bill_id;

                        set @_out_stt = 1;
                        set @_out_msg = N'Cập nhật chi tiết hóa đơn thành công';
                        if @@trancount > 0
                            commit tran
                    end

end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Cập nhật chi tiết hóa đơn thất bại' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go


create proc usp_delete_bill_detail(
    @_bill_id int,
    @_product_id int,
    @_out_stt bit =1 output,
    @_out_msg nvarchar(max) = '' output
)
as
begin try
    if not exists(select id from bill where id = @_bill_id)
        begin
            set @_out_stt = 0;
            set @_out_msg = N'Hóa đơn không tồn tại';
        end
    else
        if not exists(select id from product where id = @_product_id)
            begin
                set @_out_stt = 0;
                set @_out_msg = N'Sản phẩm không tồn tại';
            end
        else
            begin
                begin tran;
                delete bill_detail
                where bill_id = @_bill_id
                  and product_id = @_product_id;

                update bill
                set updated_at = getdate()
                where id = @_bill_id;

                set @_out_stt = 1;
                set @_out_msg = N'Xóa sản phẩm khỏi hóa đơn thành công';
                if @@trancount > 0
                    commit tran;
            end
end try
begin catch
    set @_out_stt = 0;
    set @_out_msg = N'Xóa sản phẩm khỏi hóa đơn thất bại' + error_message();
    if @@trancount > 0
        rollback tran;
end catch
go

create proc usp_get_bill_detail_by_bill(@_bill_id int)
as
select bd.*,
       p.name 'product_name'
from bill_detail bd
         left join product p on bd.product_id = p.id
where bill_id = @_bill_id
go

