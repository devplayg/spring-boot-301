class CustomLogoutHandler(
        private val memberCacheService: MemberCacheService
): LogoutHandler  {
    override fun logout(req: HttpServletRequest, res: HttpServletResponse, auth: Authentication) {
        memberCacheService.markAsOffline(auth.name)
        memberCacheService.delete2FA(req.session.id)
    }
}
